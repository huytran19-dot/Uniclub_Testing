import React, { useMemo, useState, useEffect } from "react"
import { useParams, Link, useNavigate } from "react-router-dom"
import { PageLayout } from "../components/PageLayout"
import { Button } from "@/components/ui/button"
import { VariantSelector } from "@/components/VariantSelector"
import { Price } from "@/components/Price"
import { getUserCart, addToCart as addToCartAPI } from "@/lib/cart-api"
import { ProductReviews } from "@/components/product/ProductReviews"
import { Star, Loader2 } from "lucide-react"

export default function ProductDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const productId = Number(id)

  // State for API data
  const [product, setProduct] = useState(null)
  const [productVariants, setProductVariants] = useState([])
  const [brand, setBrand] = useState(null)
  const [sizes, setSizes] = useState([])
  const [colors, setColors] = useState([])
  const [productReviews, setProductReviews] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [addingToCart, setAddingToCart] = useState(false)

  const [selectedSizeId, setSelectedSizeId] = useState(null)
  const [selectedColorId, setSelectedColorId] = useState(null)

  // Fetch product data from API
  useEffect(() => {
    const fetchProductData = async () => {
      setLoading(true)
      setError(null)

      try {
        // Fetch product details
        const productRes = await fetch(`http://localhost:8080/api/products/${productId}`)
        if (!productRes.ok) {
          if (productRes.status === 404) {
            setError('Sản phẩm không tồn tại')
          } else {
            throw new Error('Không thể tải thông tin sản phẩm')
          }
          return
        }
        const productData = await productRes.json()
        setProduct(productData)

        // Fetch all variants for this product
        const variantsRes = await fetch('http://localhost:8080/api/variants')
        if (!variantsRes.ok) throw new Error('Không thể tải biến thể sản phẩm')
        const allVariants = await variantsRes.json()
        const filteredVariants = allVariants.filter(v => v.productId === parseInt(productId) && v.status === 1)
        setProductVariants(filteredVariants)

        // Fetch brand
        if (productData.brandId) {
          const brandRes = await fetch(`http://localhost:8080/api/brands/${productData.brandId}`)
          if (brandRes.ok) {
            const brandData = await brandRes.json()
            setBrand(brandData)
          }
        }

        // Fetch sizes and colors
        const [sizesRes, colorsRes] = await Promise.all([
          fetch('http://localhost:8080/api/sizes'),
          fetch('http://localhost:8080/api/colors'),
        ])

        if (sizesRes.ok) setSizes(await sizesRes.json())
        if (colorsRes.ok) setColors(await colorsRes.json())

        // TODO: Fetch reviews when review API is ready
        // For now, reviews are empty
        setProductReviews([])

      } catch (err) {
        console.error('Error fetching product:', err)
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    fetchProductData()
  }, [productId])

  useEffect(() => {
    // Prefill first available combination
    if (productVariants.length > 0) {
      const available = productVariants.find((v) => v.quantity > 0) || productVariants[0]
      setSelectedSizeId(available.sizeId)
      setSelectedColorId(available.colorId)
    }
  }, [productVariants])

  const disabledPairs = useMemo(() => productVariants.filter((v) => v.quantity === 0), [productVariants])

  const selectedVariant = useMemo(
    () =>
      productVariants.find((v) => v.sizeId === selectedSizeId && v.colorId === selectedColorId) ||
      productVariants.find((v) => v.sizeId === selectedSizeId) ||
      productVariants.find((v) => v.colorId === selectedColorId) ||
      productVariants[0],
    [productVariants, selectedSizeId, selectedColorId],
  )

  // Get only sizes and colors that exist in product variants
  const availableSizes = useMemo(() => {
    const sizeIds = [...new Set(productVariants.map(v => v.sizeId))]
    return sizes.filter(s => sizeIds.includes(s.id) && s.status === 1)
  }, [productVariants, sizes])

  const availableColors = useMemo(() => {
    const colorIds = [...new Set(productVariants.map(v => v.colorId))]
    return colors.filter(c => colorIds.includes(c.id) && c.status === 1)
  }, [productVariants, colors])

  // Helper functions
  const getMinPrice = (variants, basePrice) => {
    if (variants.length === 0) return basePrice || 0
    const variantPrices = variants.map(v => v.price).filter(p => p !== null && p > 0)
    if (variantPrices.length === 0) return basePrice || 0
    return Math.min(...variantPrices, basePrice || Infinity)
  }

  const isOutOfStock = (variants) => {
    if (variants.length === 0) return true
    return variants.every(v => v.quantity === 0)
  }

  const calculateAverageRating = (reviews) => {
    if (reviews.length === 0) return 0
    const sum = reviews.reduce((acc, r) => acc + r.rating, 0)
    return (sum / reviews.length).toFixed(1)
  }

  // Show loading state
  if (loading) {
    return (
      <PageLayout breadcrumbs={[{ label: "Sản phẩm", href: "/products" }, { label: "Đang tải..." }]}>
        <div className="section">
          <div className="flex items-center justify-center py-20">
            <div className="text-center">
              <Loader2 className="w-12 h-12 animate-spin mx-auto mb-4 text-primary" />
              <p className="text-lg text-muted-foreground">Đang tải sản phẩm...</p>
            </div>
          </div>
        </div>
      </PageLayout>
    )
  }

  // Show error state
  if (error || !product) {
    return (
      <PageLayout breadcrumbs={[{ label: "Sản phẩm", href: "/products" }, { label: "Lỗi" }]}> 
        <div className="section">
          <div className="card p-8 text-center">
            <div className="text-lg font-medium mb-2">{error || "Sản phẩm không tồn tại"}</div>
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

  const onSelectVariant = ({ sizeId, colorId }) => {
    if (sizeId != null) setSelectedSizeId(sizeId)
    if (colorId != null) setSelectedColorId(colorId)
  }

  const handleAddToCart = async () => {
    const v = selectedVariant
    if (!v || v.quantity <= 0) return

    // Check if user is logged in
    const userStr = localStorage.getItem('uniclub_user')
    if (!userStr) {
      alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng")
      navigate('/login')
      return
    }

    try {
      setAddingToCart(true)
      const user = JSON.parse(userStr)
      
      // Get or create cart
      const cart = await getUserCart(user.id)
      
      // Add item to cart with quantity = 1
      await addToCartAPI(cart.id, v.sku, 1, v.price || product.price)
      
      alert(`Đã thêm ${product.name} vào giỏ hàng`)
      
      // Trigger cart update event for header cart badge
      window.dispatchEvent(new Event("cart-updated"))
    } catch (error) {
      console.error('Error adding to cart:', error)
      const errorMsg = error.response?.data?.message || error.message || "Không thể thêm sản phẩm vào giỏ hàng"
      alert(`Lỗi: ${errorMsg}`)
    } finally {
      setAddingToCart(false)
    }
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
                  setSelectedSizeId(v.sizeId)
                  setSelectedColorId(v.colorId)
                }}
                className={`card overflow-hidden ${
                  v.sizeId === selectedSizeId && v.colorId === selectedColorId ? "ring-2 ring-ring" : ""
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
            sizes={availableSizes}
            colors={availableColors}
            selectedSizeId={selectedSizeId}
            selectedColorId={selectedColorId}
            disabledPairs={disabledPairs}
            onSelect={onSelectVariant}
          />

          <div className="flex gap-3">
            <Button 
              onClick={handleAddToCart} 
              disabled={!selectedVariant || selectedVariant.quantity <= 0 || addingToCart}
            >
              {addingToCart ? "Đang thêm..." : "Thêm vào giỏ"}
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
