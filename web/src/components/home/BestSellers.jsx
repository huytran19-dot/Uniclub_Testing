import React, { useMemo } from "react"
import { Link } from "react-router-dom"
import { products, variants, brands, reviews } from "@/lib/mock-data"
import { calculateAverageRating, getMinPrice, isOutOfStock } from "@/lib/utils"
import { ProductCard } from "@/components/ProductCard"

export default function BestSellers() {
  const list = useMemo(() => {
    const active = products.filter((p) => p.status === 1)
    const scored = active.map((p) => {
      const r = reviews.filter((rv) => rv.id_product === p.id && rv.status === 1)
      const score = calculateAverageRating(r)
      return { product: p, score, count: r.length }
    })
    // sort by rating then review count then newest
    scored.sort((a, b) => {
      if (b.score !== a.score) return b.score - a.score
      if (b.count !== a.count) return b.count - a.count
      return new Date(b.product.created_at) - new Date(a.product.created_at)
    })
    return scored.slice(0, 6)
  }, [])

  return (
    <section className="section">
      <div className="flex items-baseline justify-between mb-4">
        <h2 className="text-2xl font-semibold tracking-tight text-foreground">Bán chạy</h2>
        <Link to="/products" className="link-underline text-sm">Xem tất cả</Link>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {list.map(({ product }) => {
          const productVariants = variants.filter((v) => v.id_product === product.id && v.status === 1)
          const productReviews = reviews.filter((r) => r.id_product === product.id && r.status === 1)
          const brand = brands.find((b) => b.id === product.id_brand)
          const firstVariant = productVariants[0]

          return (
            <ProductCard
              key={product.id}
              product={product}
              brand={brand?.name || "Không xác định"}
              image={firstVariant?.images || "/placeholder.svg?height=400&width=400"}
              minPrice={getMinPrice(productVariants, product.price)}
              rating={calculateAverageRating(productReviews)}
              reviewCount={productReviews.length}
              isOutOfStock={isOutOfStock(productVariants)}
            />
          )
        })}
      </div>
    </section>
  )
}
