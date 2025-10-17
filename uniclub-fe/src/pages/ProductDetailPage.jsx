import React, { useMemo, useState, useEffect } from "react"
import { useParams, Link, useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { products, variants, brands, sizes, colors, reviews } from "@/lib/mock-data"
import { calculateAverageRating, getMinPrice, isOutOfStock } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { VariantSelector } from "@/components/VariantSelector"
import { Price } from "@/components/Price"
import { addToCart } from "@/lib/cart"
import { ProductReviews } from "@/components/product/ProductReviews"
import { Star } from "lucide-react"

export default function ProductDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const productId = Number(id)

  const product = useMemo(() => products.find((p) => p.id === productId && p.status === 1), [productId])
  const productVariants = useMemo(() => variants.filter((v) => v.id_product === productId && v.status === 1), [productId])
  const [productReviews, setProductReviews] = useState([])
  const brand = useMemo(() => (product ? brands.find((b) => b.id === product.id_brand) : null), [product])

  const [selectedSizeId, setSelectedSizeId] = useState(null)
  const [selectedColorId, setSelectedColorId] = useState(null)

  useEffect(() => {
    // Load reviews from mock data
    const initialReviews = reviews.filter((r) => r.id_product === productId && r.status === 1)
    setProductReviews(initialReviews)
  }, [productId])

  useEffect(() => {
    // Prefill first available combination
    if (productVariants.length > 0) {
      const available = productVariants.find((v) => v.quantity > 0) || productVariants[0]
      setSelectedSizeId(available.id_size)
      setSelectedColorId(available.id_color)
    }
  }, [productVariants])

  const disabledPairs = useMemo(() => productVariants.filter((v) => v.quantity === 0), [productVariants])

  const selectedVariant = useMemo(
    () =>
      productVariants.find((v) => v.id_size === selectedSizeId && v.id_color === selectedColorId) ||
      productVariants.find((v) => v.id_size === selectedSizeId) ||
      productVariants.find((v) => v.id_color === selectedColorId) ||
      productVariants[0],
    [productVariants, selectedSizeId, selectedColorId],
  )

  if (!product) {
    return (
      <PageLayout breadcrumbs={[{ label: "Sản phẩm", href: "/products" }, { label: "Không tìm thấy" }]}> 
        <div className="section">
          <div className="card p-8 text-center">
            <div className="text-lg font-medium mb-2">Sản phẩm không tồn tại</div>
            <div className="text-muted-foreground mb-4">Vui lòng quay lại trang sản phẩm.</div>
            <Button onClick={() => navigate("/products")}>Về trang sản phẩm</Button>
          </div>
        </div>
      </PageLayout>
    )
  }

  const rating = calculateAverageRating(productReviews)
  const minPrice = getMinPrice(productVariants, product.price)
  const oos = isOutOfStock(productVariants)

  const onSelectVariant = ({ id_size, id_color }) => {
    if (id_size != null) setSelectedSizeId(id_size)
    if (id_color != null) setSelectedColorId(id_color)
  }

  const handleAddToCart = () => {
    const v = selectedVariant
    if (!v || v.quantity <= 0) return
    const sizeObj = sizes.find((s) => s.id === v.id_size)
    const colorObj = colors.find((c) => c.id === v.id_color)

    addToCart({
      sku_variant: v.sku,
      productName: product.name,
      sizeName: sizeObj?.name || "N/A",
      colorName: colorObj?.name || "N/A",
      unitPrice: v.price || product.price,
      image: v.images,
      maxQuantity: v.quantity,
    })
    window.dispatchEvent(new Event("cart-updated"))
  }

  return (
    <PageLayout
      breadcrumbs={[
        { label: "Sản phẩm", href: "/products" },
        { label: product.name },
      ]}
    >
      <div className="section grid grid-cols-1 lg:grid-cols-2 gap-10">
        {/* Gallery */}
        <div>
          <div className="card overflow-hidden">
            <div className="aspect-square bg-surface">
              <img src={selectedVariant?.images || "/placeholder.svg"} alt={product.name} className="w-full h-full object-cover" />
            </div>
          </div>
          <div className="mt-3 grid grid-cols-4 gap-3">
            {productVariants.slice(0, 8).map((v) => (
              <button
                key={v.sku}
                onClick={() => {
                  setSelectedSizeId(v.id_size)
                  setSelectedColorId(v.id_color)
                }}
                className={`card overflow-hidden ${
                  v.id_size === selectedSizeId && v.id_color === selectedColorId ? "ring-2 ring-ring" : ""
                }`}
              >
                <div className="aspect-square bg-surface">
                  <img src={v.images} alt="thumb" className="w-full h-full object-cover" />
                </div>
              </button>
            ))}
          </div>
        </div>

        {/* Info */}
        <div className="space-y-5">
          <div>
            <div className="text-sm text-muted-foreground mb-1">{brand?.name || "Thương hiệu"}</div>
            <h1 className="text-2xl md:text-3xl font-bold text-foreground">{product.name}</h1>
            {productReviews.length > 0 && (
              <div className="flex items-center gap-2 mt-2 text-sm">
                <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                <span className="text-foreground font-medium">{rating}</span>
                <span className="text-muted-foreground">({productReviews.length} đánh giá)</span>
              </div>
            )}
          </div>

          <div className="text-3xl font-bold text-primary">
            {selectedVariant?.price ? <Price value={selectedVariant.price} /> : <Price value={minPrice} />}
          </div>

          <VariantSelector
            sizes={sizes.filter((s) => s.status === 1)}
            colors={colors.filter((c) => c.status === 1)}
            selectedSizeId={selectedSizeId}
            selectedColorId={selectedColorId}
            disabledPairs={disabledPairs}
            onSelect={onSelectVariant}
          />

          <div className="flex gap-3">
            <Button onClick={handleAddToCart} disabled={!selectedVariant || selectedVariant.quantity <= 0}>
              Thêm vào giỏ
            </Button>
            <Button variant="outline" aschild="true">
              <Link to="/cart">Xem giỏ hàng</Link>
            </Button>
          </div>

          <div className="pt-4 border-t border-border">
            <div className="text-foreground font-medium mb-2">Mô tả</div>
            <p className="text-muted-foreground text-sm leading-relaxed">{product.description}</p>
          </div>

          <div className="pt-4 border-t border-border">
            <div className="text-foreground font-medium mb-2">Thông tin</div>
            <p className="text-muted-foreground text-sm leading-relaxed">{product.information}</p>
          </div>
        </div>
      </div>

      {/* Reviews Section */}
      <div className="section">
        <ProductReviews
          productId={productId}
          reviews={productReviews}
          onAddReview={(newReview) => {
            setProductReviews([newReview, ...productReviews])
          }}
        />
      </div>
    </PageLayout>
  )
}
