"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import { ArrowRight } from "lucide-react"
import { Button } from "@/components/ui/button"
import { ProductCard } from "@/components/product-card"
import { products, variants, brands, categories, reviews } from "@/lib/mock-data"
import { calculateAverageRating, getMinPrice, isOutOfStock } from "@/lib/utils"
import { addToCart } from "@/lib/cart"
import { motion } from "framer-motion"

export default function HomePage() {
  const [mounted, setMounted] = useState(false)

  useEffect(() => {
    setMounted(true)
  }, [])

  // Get active categories and brands
  const activeCategories = categories.filter((c) => c.status === 1)
  const activeBrands = brands.filter((b) => b.status === 1)

  // Get new arrivals (sorted by created_at desc)
  const newArrivals = [...products]
    .filter((p) => p.status === 1)
    .sort((a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime())
    .slice(0, 4)

  // Get best sellers (mock selection - first 4 products)
  const bestSellers = products.filter((p) => p.status === 1).slice(0, 4)

  const getProductData = (product: (typeof products)[0]) => {
    const productVariants = variants.filter((v) => v.id_product === product.id && v.status === 1)
    const productReviews = reviews.filter((r) => r.id_product === product.id && r.status === 1)
    const brand = brands.find((b) => b.id === product.id_brand)
    const firstVariant = productVariants[0]

    return {
      variants: productVariants,
      brand: brand?.name || "Không xác định",
      image: firstVariant?.images || "/placeholder.svg?height=400&width=400",
      minPrice: getMinPrice(productVariants, product.price),
      rating: calculateAverageRating(productReviews),
      reviewCount: productReviews.length,
      isOutOfStock: isOutOfStock(productVariants),
    }
  }

  const handleAddToCart = (product: (typeof products)[0]) => {
    const productVariants = variants.filter((v) => v.id_product === product.id && v.status === 1)
    const availableVariant = productVariants.find((v) => v.quantity > 0)

    if (!availableVariant) return

    const size = availableVariant.id_size
    const color = availableVariant.id_color
    const sizeName = size === 2 ? "M" : size === 3 ? "L" : "S"
    const colorName = color === 1 ? "Black" : color === 2 ? "White" : color === 3 ? "Navy" : "Red"

    addToCart({
      sku_variant: availableVariant.sku,
      productName: product.name,
      sizeName,
      colorName,
      unitPrice: availableVariant.price || product.price,
      image: availableVariant.images,
      maxQuantity: availableVariant.quantity,
    })

    window.dispatchEvent(new Event("cart-updated"))
  }

  if (!mounted) return null

  return (
    <div className="min-h-screen bg-background">
      {/* Hero Section */}
      <section className="bg-gradient-to-br from-primary/10 via-background to-background py-20">
        <div className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            className="max-w-3xl mx-auto text-center"
          >
            <h1 className="text-5xl md:text-6xl font-bold text-foreground mb-6 text-balance">
              Thời trang chất lượng cao
            </h1>
            <p className="text-xl text-muted-foreground mb-8 text-pretty leading-relaxed">
              Khám phá bộ sưu tập mới nhất từ UniClub - Nơi phong cách gặp gỡ chất lượng
            </p>
            <Link href="/products">
              <Button size="lg" className="rounded-full px-8">
                Khám phá ngay
                <ArrowRight className="w-5 h-5 ml-2" />
              </Button>
            </Link>
          </motion.div>
        </div>
      </section>

      {/* Categories Grid */}
      <section className="container mx-auto px-4 py-16">
        <h2 className="text-3xl font-bold text-foreground mb-8">Danh mục</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {activeCategories.map((category, index) => (
            <motion.div
              key={category.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.4, delay: index * 0.1 }}
            >
              <Link href={`/products?category=${category.id}`}>
                <div className="group relative h-64 rounded-2xl overflow-hidden bg-surface border border-border hover:shadow-lg transition-shadow">
                  <div className="absolute inset-0 bg-gradient-to-br from-primary/20 to-primary/5 group-hover:from-primary/30 group-hover:to-primary/10 transition-colors" />
                  <div className="absolute inset-0 flex items-center justify-center">
                    <h3 className="text-3xl font-bold text-foreground group-hover:scale-110 transition-transform">
                      {category.name}
                    </h3>
                  </div>
                </div>
              </Link>
            </motion.div>
          ))}
        </div>
      </section>

      {/* Brand Strip */}
      <section className="bg-surface py-12 border-y border-border">
        <div className="container mx-auto px-4">
          <h2 className="text-2xl font-bold text-foreground mb-6 text-center">Thương hiệu</h2>
          <div className="flex flex-wrap justify-center items-center gap-12">
            {activeBrands.map((brand) => (
              <Link
                key={brand.id}
                href={`/products?brand=${brand.id}`}
                className="text-xl font-semibold text-muted-foreground hover:text-primary transition-colors"
              >
                {brand.name}
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* New Arrivals */}
      <section className="container mx-auto px-4 py-16">
        <div className="flex items-center justify-between mb-8">
          <h2 className="text-3xl font-bold text-foreground">Sản phẩm mới</h2>
          <Link href="/products?sort=newest">
            <Button variant="ghost">
              Xem tất cả
              <ArrowRight className="w-4 h-4 ml-2" />
            </Button>
          </Link>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {newArrivals.map((product) => {
            const data = getProductData(product)
            return (
              <ProductCard
                key={product.id}
                product={product}
                brand={data.brand}
                image={data.image}
                minPrice={data.minPrice}
                rating={data.rating}
                reviewCount={data.reviewCount}
                isOutOfStock={data.isOutOfStock}
                onAddToCart={() => handleAddToCart(product)}
              />
            )
          })}
        </div>
      </section>

      {/* Best Sellers */}
      <section className="container mx-auto px-4 py-16 bg-surface/50 rounded-3xl mb-16">
        <div className="flex items-center justify-between mb-8">
          <h2 className="text-3xl font-bold text-foreground">Bán chạy nhất</h2>
          <Link href="/products">
            <Button variant="ghost">
              Xem tất cả
              <ArrowRight className="w-4 h-4 ml-2" />
            </Button>
          </Link>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {bestSellers.map((product) => {
            const data = getProductData(product)
            return (
              <ProductCard
                key={product.id}
                product={product}
                brand={data.brand}
                image={data.image}
                minPrice={data.minPrice}
                rating={data.rating}
                reviewCount={data.reviewCount}
                isOutOfStock={data.isOutOfStock}
                onAddToCart={() => handleAddToCart(product)}
              />
            )
          })}
        </div>
      </section>
    </div>
  )
}
