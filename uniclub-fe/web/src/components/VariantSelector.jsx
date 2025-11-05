"use client"

import { cn } from "../lib/utils"
import { Button } from "./ui/button"

export function VariantSelector({ sizes, colors, selectedSizeId, selectedColorId, disabledPairs, allVariants, onSelect }) {
  // Không disable bất kỳ size nào - cho phép chọn tất cả
  const isSizeDisabled = (sizeId) => {
    return false  // ✅ Luôn cho phép chọn size
  }

  // Chỉ hiển thị màu của size đã chọn
  const isColorDisabled = (colorId) => {
    if (!selectedSizeId) return true  // Chưa chọn size → disable tất cả màu
    
    // Kiểm tra xem có variant nào với size đã chọn và màu này không
    const variantExists = allVariants?.some((v) => 
      v.sizeId === selectedSizeId && v.colorId === colorId
    )
    
    return !variantExists  // Disable nếu không có variant với combination này
  }

  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-sm font-medium text-foreground mb-3">Kích thước</h3>
        <div className="flex flex-wrap gap-2">
          {sizes.map((size) => {
            const disabled = isSizeDisabled(size.id)
            const selected = selectedSizeId === size.id

            return (
              <Button
                key={size.id}
                variant={selected ? "default" : "outline"}
                size="sm"
                disabled={disabled}
                onClick={() => onSelect({ sizeId: size.id, colorId: selectedColorId })}
                className={cn("rounded-full min-w-12", disabled && "opacity-50 cursor-not-allowed")}
              >
                {size.name}
              </Button>
            )
          })}
        </div>
      </div>

      <div>
        <h3 className="text-sm font-medium text-foreground mb-3">Màu sắc</h3>
        <div className="flex flex-wrap gap-2">
          {colors.map((color) => {
            const disabled = isColorDisabled(color.id)
            const selected = selectedColorId === color.id

            return (
              <Button
                key={color.id}
                variant={selected ? "default" : "outline"}
                size="sm"
                disabled={disabled}
                onClick={() => onSelect({ sizeId: selectedSizeId, colorId: color.id })}
                className={cn("rounded-full", disabled && "opacity-50 cursor-not-allowed")}
              >
                {color.name}
              </Button>
            )
          })}
        </div>
      </div>
    </div>
  )
}
