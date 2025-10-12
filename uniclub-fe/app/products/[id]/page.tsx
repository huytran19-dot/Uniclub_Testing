"use client"

import { useEffect, useState } from "react"
import { useParams, useRouter } from "next/navigation"
import Image from "next/image"
import { Star, ShoppingCart, Minus, Plus, Package } from "lucide-react"
import { PageLayout } from "@/components/page-layout"
import { VariantSelector } from "@/components/variant-selector"
import { Price } from "@/components/price"
import { Button } from "@/components/ui/button"
import { Textarea } from "@/components/ui/textarea"
import { products, variants, brands, categories, sizes, colors, reviews, users } from "@/lib/mock-data"
import { calculateAverageRating } from "@/lib/utils"
import { addToCart } from "@/lib/cart"
import { getCurrentUser } from "@/lib/auth"

export default function ProductDetailPage() {
  const params = useParams()
  const router = useRouter()
  const productId = Number(params.id)

  const [mounted, setMounted] = useState(false)
  const [selectedSizeId, setSelectedSizeId] = useState<number | null>(null)
  const [selectedColorId, setSelectedColorId] = useState<number | null>(null)
  const [quantity, setQuantity] = useState(1)
  const [selectedImage, setSelectedImage] = useState(0)
  const [reviewContent, setReviewContent] = useState("")
  const [reviewStar, setReviewStar] = useState(5)

  useEffect(() => {
    setMounted(true)
  }, [])

  if (!mounted) return null

  const product = products.find((p) => p.id === productId && p.status === 1)

  if (!product) {
    return (
      <PageLayout title="Không tìm thấy sản phẩm" breadcrumbs={[{ label: "Sản phẩm", href: "/products" }]}>
        <div className="text-center py-16">
          <p className="text-lg text-muted-foreground mb-4">Sản phẩm không tồn tại hoặc đã bị xóa</p>
          <Button onClick={() => router.push("/products")}>Quay lại danh sách</Button>
        </div>
      </PageLayout>
    )
  }

  const brand = brands.find((b) => b.id === product.id_brand)
  const category = categories.find((c) => c.id === product.id_category)
  const productVariants = variants.filter((v) => v.id_product === product.id && v.status === 1)
  const productReviews = reviews.filter((r) => r.id_product === product.id && r.status === 1)

  // Get unique sizes and colors from variants
  const availableSizes = Array.from(new Set(productVariants.map((v) => v.id_size)))
    .map((id) => sizes.find((s) => s.id === id))
    .filter(Boolean) as typeof sizes

  const availableColors = Array.from(new Set(productVariants.map((v) => v.id_color)))
    .map((id) => colors.find((c) => c.id === id))
    .filter(Boolean) as typeof colors

  // Get disabled pairs (combinations that don't exist)
  const allPairs = availableSizes.flatMap((size) =>
    availableColors.map((color) => ({ id_size: size.id, id_color: color.id })),
  )

  const existingPairs = productVariants.map((v) => ({ id_size: v.id_size, id_color: v.id_color }))

  const disabledPairs = allPairs.filter(
    (pair) => !existingPairs.some((ep) => ep.id_size === pair.id_size && ep.id_color === pair.id_color),
  )

  // Get selected variant
  const selectedVariant =
    selectedSizeId && selectedColorId
      ? productVariants.find((v) => v.id_size === selectedSizeId && v.id_color === selectedColorId)
      : null

  const currentPrice = selectedVariant?.price || product.price
  const currentStock = selectedVariant?.quantity || 0
  const isOutOfStock = selectedVariant ? currentStock === 0 : productVariants.every((v) => v.quantity === 0)

  // Get all images from variants
  const allImages = Array.from(new Set(productVariants.map((v) => v.images)))

  const averageRating = calculateAverageRating(productReviews)

  const handleVariantSelect = ({ id_size, id_color }: { id_size: number | null; id_color: number | null }) => {
    setSelectedSizeId(id_size)
    setSelectedColorId(id_color)
    setQuantity(1)
  }

  const handleAddToCart = () => {
    if (!selectedVariant || !selectedSizeId || !selectedColorId) return

    const size = sizes.find((s) => s.id === selectedSizeId)
    const color = colors.find((c) => c.id === selectedColorId)

    addToCart({
      sku_variant: selectedVariant.sku,
      productName: product.name,
      sizeName: size?.name || "N/A",
      colorName: color?.name || "N/A",
      unitPrice: currentPrice,
      image: selectedVariant.images,
      maxQuantity: currentStock,
      qty: quantity,
    })

    window.dispatchEvent(new Event("cart-updated"))
    router.push("/cart")
  }

  const handleBuyNow = () => {
    handleAddToCart()
  }

  const handleSubmitReview = () => {
    const user = getCurrentUser()
    if (!user) {
      router.push("/login")
      return
    }

    // Mock review submission
    const newReview = {
      id: reviews.length + 1,
      id_product: product.id,
      id_user: user.id,
      star: reviewStar,
      content: reviewContent,
      images: "",
      status: 1,
      created_at: new Date().toISOString(),
      updated_at: new Date().toISOString(),
    }

    reviews.push(newReview)
    setReviewContent("")
    setReviewStar(5)
  }

  return (
    <PageLayout
      title={product.name}
      breadcrumbs={[
        { label: "Sản phẩm", href: "/products" },
        { label: category?.name || "Danh mục", href: `/products?category=${product.id_category}` },
        { label: product.name },
      ]}
    >
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 mb-16">
        {/* Left: Gallery */}
        <div className="space-y-4">
          <div className="aspect-square rounded-2xl overflow-hidden bg-surface border border-border">
            <Image
              src={allImages[selectedImage] || "/placeholder.svg?height=600&width=600"}
              alt={product.name}
              width={600}
              height={600}
              className="w-full h-full object-cover"
            />
          </div>
          {allImages.length > 1 && (
            <div className="grid grid-cols-4 gap-4">
              {allImages.map((image, index) => (
                <button
                  key={index}
                  onClick={() => setSelectedImage(index)}
                  className={cn(
                    "aspect-square rounded-lg overflow-hidden border-2 transition-colors",
                    selectedImage === index ? "border-primary" : "border-border hover:border-primary/50",
                  )}
                >
                  <Image
                    src={image || "/placeholder.svg"}
                    alt={`${product.name} ${index + 1}`}
                    width={150}
                    height={150}
                    className="w-full h-full object-cover"
                  />
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Right: Product Info */}
        <div className="space-y-6">
          <div>
            <div className="text-sm text-muted-foreground mb-2">{brand?.name || "Không xác định"}</div>
            <h1 className="text-3xl font-bold text-foreground mb-2">{product.name}</h1>
            <div className="text-sm text-muted-foreground">{category?.name || "Không xác định"}</div>
          </div>

          {/* Rating */}
          {productReviews.length > 0 && (
            <div className="flex items-center gap-2">
              <div className="flex items-center gap-1">
                <Star className="w-5 h-5 fill-yellow-400 text-yellow-400" />
                <span className="font-semibold text-foreground">{averageRating}</span>
              </div>
              <span className="text-sm text-muted-foreground">({productReviews.length} đánh giá)</span>
            </div>
          )}

          {/* Price */}
          <div className="py-4 border-y border-border">
            <Price value={currentPrice} className="text-3xl font-bold text-primary" />
          </div>

          {/* Description */}
          <div>
            <h3 className="text-sm font-medium text-foreground mb-2">Mô tả</h3>
            <p className="text-sm text-muted-foreground leading-relaxed">{product.description}</p>
          </div>

          {/* Information */}
          <div>
            <h3 className="text-sm font-medium text-foreground mb-2">Thông tin</h3>
            <p className="text-sm text-muted-foreground">{product.information}</p>
          </div>

          {/* Variant Selector */}
          <VariantSelector
            sizes={availableSizes}
            colors={availableColors}
            selectedSizeId={selectedSizeId}
            selectedColorId={selectedColorId}
            disabledPairs={disabledPairs}
            onSelect={handleVariantSelect}
          />

          {/* Stock Status */}
          {selectedVariant && (
            <div className="flex items-center gap-2 text-sm">
              <Package className="w-4 h-4" />
              {isOutOfStock ? (
                <span className="text-danger font-medium">Hết hàng</span>
              ) : (
                <span className="text-success font-medium">Còn {currentStock} sản phẩm</span>
              )}
            </div>
          )}

          {/* Quantity Selector */}
          {selectedVariant && !isOutOfStock && (
            <div>
              <h3 className="text-sm font-medium text-foreground mb-3">Số lượng</h3>
              <div className="flex items-center gap-3">
                <Button
                  variant="outline"
                  size="icon"
                  onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  disabled={quantity <= 1}
                >
                  <Minus className="w-4 h-4" />
                </Button>
                <span className="text-lg font-medium w-12 text-center">{quantity}</span>
                <Button
                  variant="outline"
                  size="icon"
                  onClick={() => setQuantity(Math.min(currentStock, quantity + 1))}
                  disabled={quantity >= currentStock}
                >
                  <Plus className="w-4 h-4" />
                </Button>
              </div>
            </div>
          )}

          {/* Actions */}
          <div className="flex gap-4">
            <Button
              size="lg"
              variant="outline"
              className="flex-1 bg-transparent"
              disabled={!selectedVariant || isOutOfStock}
              onClick={handleAddToCart}
            >
              <ShoppingCart className="w-5 h-5 mr-2" />
              Thêm vào giỏ
            </Button>
            <Button size="lg" className="flex-1" disabled={!selectedVariant || isOutOfStock} onClick={handleBuyNow}>
              Mua ngay
            </Button>
          </div>

          {!selectedVariant && (
            <p className="text-sm text-muted-foreground text-center">Vui lòng chọn kích thước và màu sắc</p>
          )}
        </div>
      </div>

      {/* Reviews Section */}
      <div className="border-t border-border pt-12">
        <h2 className="text-2xl font-bold text-foreground mb-8">Đánh giá sản phẩm</h2>

        {/* Review Form */}
        <div className="bg-surface rounded-2xl border border-border p-6 mb-8">
          <h3 className="font-semibold text-foreground mb-4">Viết đánh giá</h3>
          <div className="space-y-4">
            <div>
              <label className="text-sm font-medium text-foreground mb-2 block">Đánh giá của bạn</label>
              <div className="flex gap-2">
                {[1, 2, 3, 4, 5].map((star) => (
                  <button key={star} onClick={() => setReviewStar(star)}>
                    <Star
                      className={cn(
                        "w-6 h-6",
                        star <= reviewStar ? "fill-yellow-400 text-yellow-400" : "text-gray-300",
                      )}
                    />
                  </button>
                ))}
              </div>
            </div>
            <div>
              <label className="text-sm font-medium text-foreground mb-2 block">Nội dung</label>
              <Textarea
                placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm..."
                value={reviewContent}
                onChange={(e) => setReviewContent(e.target.value)}
                rows={4}
              />
            </div>
            <Button onClick={handleSubmitReview} disabled={!reviewContent.trim()}>
              Gửi đánh giá
            </Button>
          </div>
        </div>

        {/* Review List */}
        <div className="space-y-6">
          {productReviews.length === 0 ? (
            <p className="text-center text-muted-foreground py-8">Chưa có đánh giá nào</p>
          ) : (
            productReviews.map((review) => {
              const reviewer = users.find((u) => u.id === review.id_user)
              return (
                <div key={review.id} className="bg-white rounded-2xl border border-border p-6">
                  <div className="flex items-start justify-between mb-3">
                    <div>
                      <div className="font-medium text-foreground">{reviewer?.full_name || "Người dùng"}</div>
                      <div className="flex items-center gap-1 mt-1">
                        {Array.from({ length: 5 }).map((_, i) => (
                          <Star
                            key={i}
                            className={cn(
                              "w-4 h-4",
                              i < review.star ? "fill-yellow-400 text-yellow-400" : "text-gray-300",
                            )}
                          />
                        ))}
                      </div>
                    </div>
                    <div className="text-sm text-muted-foreground">
                      {new Date(review.created_at).toLocaleDateString("vi-VN")}
                    </div>
                  </div>
                  <p className="text-sm text-muted-foreground leading-relaxed">{review.content}</p>
                </div>
              )
            })
          )}
        </div>
      </div>
    </PageLayout>
  )
}

function cn(...classes: (string | boolean | undefined)[]) {
  return classes.filter(Boolean).join(" ")
}
