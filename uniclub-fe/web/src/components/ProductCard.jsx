"use client"

import { Star } from "lucide-react"
import { Link } from "react-router-dom"
import { Price } from "./Price"
import { cn } from "../lib/utils"

export function ProductCard({
  product,
  brand,
  image,
  minPrice,
  rating,
  reviewCount,
  isOutOfStock = false,
  className,
}) {
  return (
    <div className={cn("group", className)}>
      <Link to={`/products/${product.id}`}>
        <div className="card card-hover overflow-hidden">
          <div className="relative aspect-square bg-surface overflow-hidden">
            <img
              src={image || "/placeholder.svg"}
              alt={product.name}
              className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            />
            {isOutOfStock && (
              <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                <span className="bg-white text-foreground px-4 py-2 rounded-full text-sm font-medium">Hết hàng</span>
              </div>
            )}
          </div>

          <div className="p-4">
            <div className="text-xs text-muted-foreground mb-1">{brand}</div>
            <h3 className="font-semibold text-foreground mb-2 line-clamp-2 group-hover:text-primary transition-colors">
              {product.name}
            </h3>

            {reviewCount > 0 && (
              <div className="flex items-center gap-1 mb-2">
                <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                <span className="text-sm font-medium text-foreground">{rating}</span>
                <span className="text-xs text-muted-foreground">({reviewCount})</span>
              </div>
            )}

            <div className="flex items-center justify-between">
              <Price value={minPrice} className="text-lg font-bold text-primary" />
            </div>
          </div>
        </div>
      </Link>
    </div>
  )
}

