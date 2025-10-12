"use client"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"

interface VariantOption {
  id: number
  name: string
}

interface VariantSelectorProps {
  sizes: VariantOption[]
  colors: VariantOption[]
  selectedSizeId: number | null
  selectedColorId: number | null
  disabledPairs: { id_size: number; id_color: number }[]
  onSelect: (selection: { id_size: number | null; id_color: number | null }) => void
}

export function VariantSelector({
  sizes,
  colors,
  selectedSizeId,
  selectedColorId,
  disabledPairs,
  onSelect,
}: VariantSelectorProps) {
  const isSizeDisabled = (sizeId: number) => {
    if (!selectedColorId) return false
    return disabledPairs.some((pair) => pair.id_size === sizeId && pair.id_color === selectedColorId)
  }

  const isColorDisabled = (colorId: number) => {
    if (!selectedSizeId) return false
    return disabledPairs.some((pair) => pair.id_size === selectedSizeId && pair.id_color === colorId)
  }

  return (
    <div className="space-y-6">
      {/* Size Selector */}
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
                onClick={() => onSelect({ id_size: size.id, id_color: selectedColorId })}
                className={cn("rounded-full min-w-12", disabled && "opacity-50 cursor-not-allowed")}
              >
                {size.name}
              </Button>
            )
          })}
        </div>
      </div>

      {/* Color Selector */}
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
                onClick={() => onSelect({ id_size: selectedSizeId, id_color: color.id })}
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
