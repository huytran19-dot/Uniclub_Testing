import React from "react"
import { Link } from "react-router-dom"
import { Trash2, Plus, Minus } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Price } from "@/components/Price"

export function CartLineItem({ item, onUpdateQty, onRemove }) {
  // Support both qty and quantity for backward compatibility
  const quantity = item.quantity || item.qty || 0
  const { sku_variant, productName, sizeName, colorName, unitPrice, image, maxQuantity } = item

  return (
    <div className="card p-4 flex gap-4">
      <Link to={`/products/${sku_variant}`} className="flex-shrink-0">
        <div className="w-24 h-24 rounded-lg overflow-hidden bg-surface">
          <img src={image || "/placeholder.svg"} alt={productName} className="w-full h-full object-cover" />
        </div>
      </Link>
      <div className="flex-1 min-w-0">
        <div className="flex items-start justify-between gap-2">
          <div>
            <Link to={`/products/${sku_variant}`} className="font-medium text-foreground hover:text-primary line-clamp-1">
              {productName}
            </Link>
            <div className="text-sm text-muted-foreground mt-1">
              {sizeName} / {colorName}
            </div>
          </div>
          <Button variant="ghost" size="icon" onClick={() => onRemove(sku_variant)} className="flex-shrink-0">
            <Trash2 className="w-4 h-4" />
          </Button>
        </div>
        <div className="flex items-center justify-between mt-3">
          <div className="flex items-center gap-1">
            <Button
              variant="outline"
              size="icon"
              onClick={() => onUpdateQty(sku_variant, quantity - 1)}
              disabled={quantity <= 1}
              className="h-9 w-9 rounded-lg hover:bg-primary hover:text-white hover:border-primary transition-colors disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:bg-transparent disabled:hover:text-foreground"
            >
              <Minus className="w-4 h-4" />
            </Button>
            <div className="w-14 h-9 flex items-center justify-center border border-border rounded-lg bg-muted/30">
              <span className="text-sm font-semibold text-foreground">{quantity}</span>
            </div>
            <Button
              variant="outline"
              size="icon"
              onClick={() => onUpdateQty(sku_variant, quantity + 1)}
              disabled={quantity >= maxQuantity}
              className="h-9 w-9 rounded-lg hover:bg-primary hover:text-white hover:border-primary transition-colors disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:bg-transparent disabled:hover:text-foreground"
            >
              <Plus className="w-4 h-4" />
            </Button>
            {quantity >= maxQuantity && (
              <span className="text-xs text-warning ml-2">Tối đa</span>
            )}
          </div>
          <div className="font-semibold text-foreground">
            <Price value={unitPrice * quantity} />
          </div>
        </div>
      </div>
    </div>
  )
}
