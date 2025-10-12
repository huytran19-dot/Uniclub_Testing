"use client"

import { Star, ShoppingCart } from "lucide-react"
import Link from "next/link"
import Image from "next/image"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/price"
import { cn } from "@/lib/utils"
import { motion } from "framer-motion"

interface ProductCardProps {
  product: {
    id: number
    name: string
    price: number
  }
  brand: string
  image: string
  minPrice: number
  rating: number
  reviewCount: number
  isOutOfStock?: boolean
  onAddToCart?: () => void
  className?: string
}

export function ProductCard({
  product,
  brand,
  image,
  minPrice,
  rating,
  reviewCount,
  isOutOfStock = false,
  onAddToCart,
  className,
}: ProductCardProps) {
  return (
    <motion.div whileHover={{ y: -4 }} transition={{ duration: 0.2 }} className={cn("group", className)}>
      <Link href={`/products/${product.id}`}>
        <div className="bg-white rounded-2xl shadow-sm hover:shadow-md transition-shadow overflow-hidden border border-border">
          {/* Image */}
          <div className="relative aspect-square bg-surface overflow-hidden">
            <Image
              src={image || "/placeholder.svg"}
              alt={product.name}
              fill
              className="object-cover group-hover:scale-105 transition-transform duration-300"
            />
            {isOutOfStock && (
              <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                <span className="bg-white text-foreground px-4 py-2 rounded-full text-sm font-medium">Hết hàng</span>
              </div>
            )}
          </div>

          {/* Content */}
          <div className="p-4">
            <div className="text-xs text-muted-foreground mb-1">{brand}</div>
            <h3 className="font-semibold text-foreground mb-2 line-clamp-2 group-hover:text-primary transition-colors">
              {product.name}
            </h3>

            {/* Rating */}
            {reviewCount > 0 && (
              <div className="flex items-center gap-1 mb-2">
                <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                <span className="text-sm font-medium text-foreground">{rating}</span>
                <span className="text-xs text-muted-foreground">({reviewCount})</span>
              </div>
            )}

            {/* Price */}
            <div className="flex items-center justify-between">
              <Price value={minPrice} className="text-lg font-bold text-primary" />

              {!isOutOfStock && onAddToCart && (
                <Button
                  size="icon"
                  variant="ghost"
                  onClick={(e) => {
                    e.preventDefault()
                    onAddToCart()
                  }}
                  className="opacity-0 group-hover:opacity-100 transition-opacity"
                >
                  <ShoppingCart className="w-4 h-4" />
                  <span className="sr-only">Thêm vào giỏ</span>
                </Button>
              )}
            </div>
          </div>
        </div>
      </Link>
    </motion.div>
  )
}
