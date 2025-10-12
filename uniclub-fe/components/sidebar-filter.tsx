"use client"

import { X, SlidersHorizontal } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import { Label } from "@/components/ui/label"
import { Slider } from "@/components/ui/slider"
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "@/components/ui/sheet"
import { formatPrice } from "@/lib/utils"

export interface FilterState {
  categories: number[]
  brands: number[]
  sizes: number[]
  colors: number[]
  priceRange: [number, number]
  stockOnly: boolean
}

interface SidebarFilterProps {
  categories: { id: number; name: string }[]
  brands: { id: number; name: string }[]
  sizes: { id: number; name: string }[]
  colors: { id: number; name: string }[]
  priceRange: [number, number]
  filters: FilterState
  onChange: (filters: FilterState) => void
}

function FilterContent({ categories, brands, sizes, colors, priceRange, filters, onChange }: SidebarFilterProps) {
  const toggleFilter = (type: keyof FilterState, id: number) => {
    const current = filters[type] as number[]
    const updated = current.includes(id) ? current.filter((i) => i !== id) : [...current, id]
    onChange({ ...filters, [type]: updated })
  }

  const clearFilters = () => {
    onChange({
      categories: [],
      brands: [],
      sizes: [],
      colors: [],
      priceRange,
      stockOnly: false,
    })
  }

  const hasActiveFilters =
    filters.categories.length > 0 ||
    filters.brands.length > 0 ||
    filters.sizes.length > 0 ||
    filters.colors.length > 0 ||
    filters.stockOnly ||
    filters.priceRange[0] !== priceRange[0] ||
    filters.priceRange[1] !== priceRange[1]

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h3 className="font-semibold text-foreground">Bộ lọc</h3>
        {hasActiveFilters && (
          <Button variant="ghost" size="sm" onClick={clearFilters}>
            <X className="w-4 h-4 mr-1" />
            Xóa
          </Button>
        )}
      </div>

      {/* Category Filter */}
      <div className="space-y-3">
        <h4 className="text-sm font-medium text-foreground">Danh mục</h4>
        {categories.map((category) => (
          <div key={category.id} className="flex items-center gap-2">
            <Checkbox
              id={`category-${category.id}`}
              checked={filters.categories.includes(category.id)}
              onCheckedChange={() => toggleFilter("categories", category.id)}
            />
            <Label htmlFor={`category-${category.id}`} className="text-sm text-muted-foreground cursor-pointer">
              {category.name}
            </Label>
          </div>
        ))}
      </div>

      {/* Brand Filter */}
      <div className="space-y-3">
        <h4 className="text-sm font-medium text-foreground">Thương hiệu</h4>
        {brands.map((brand) => (
          <div key={brand.id} className="flex items-center gap-2">
            <Checkbox
              id={`brand-${brand.id}`}
              checked={filters.brands.includes(brand.id)}
              onCheckedChange={() => toggleFilter("brands", brand.id)}
            />
            <Label htmlFor={`brand-${brand.id}`} className="text-sm text-muted-foreground cursor-pointer">
              {brand.name}
            </Label>
          </div>
        ))}
      </div>

      {/* Size Filter */}
      <div className="space-y-3">
        <h4 className="text-sm font-medium text-foreground">Kích thước</h4>
        <div className="flex flex-wrap gap-2">
          {sizes.map((size) => (
            <Button
              key={size.id}
              variant={filters.sizes.includes(size.id) ? "default" : "outline"}
              size="sm"
              onClick={() => toggleFilter("sizes", size.id)}
              className="rounded-full"
            >
              {size.name}
            </Button>
          ))}
        </div>
      </div>

      {/* Color Filter */}
      <div className="space-y-3">
        <h4 className="text-sm font-medium text-foreground">Màu sắc</h4>
        {colors.map((color) => (
          <div key={color.id} className="flex items-center gap-2">
            <Checkbox
              id={`color-${color.id}`}
              checked={filters.colors.includes(color.id)}
              onCheckedChange={() => toggleFilter("colors", color.id)}
            />
            <Label htmlFor={`color-${color.id}`} className="text-sm text-muted-foreground cursor-pointer">
              {color.name}
            </Label>
          </div>
        ))}
      </div>

      {/* Price Range */}
      <div className="space-y-3">
        <h4 className="text-sm font-medium text-foreground">Giá</h4>
        <div className="px-2">
          <Slider
            min={priceRange[0]}
            max={priceRange[1]}
            step={10000}
            value={filters.priceRange}
            onValueChange={(value) => onChange({ ...filters, priceRange: value as [number, number] })}
            className="mb-4"
          />
          <div className="flex items-center justify-between text-sm text-muted-foreground">
            <span>{formatPrice(filters.priceRange[0])}</span>
            <span>{formatPrice(filters.priceRange[1])}</span>
          </div>
        </div>
      </div>

      {/* Stock Filter */}
      <div className="flex items-center gap-2">
        <Checkbox
          id="stock-only"
          checked={filters.stockOnly}
          onCheckedChange={(checked) => onChange({ ...filters, stockOnly: checked as boolean })}
        />
        <Label htmlFor="stock-only" className="text-sm text-muted-foreground cursor-pointer">
          Chỉ hiển thị còn hàng
        </Label>
      </div>
    </div>
  )
}

export function SidebarFilter(props: SidebarFilterProps) {
  return (
    <>
      {/* Desktop Sidebar */}
      <div className="hidden lg:block w-64 flex-shrink-0">
        <div className="sticky top-20 bg-white rounded-2xl border border-border p-6 shadow-sm">
          <FilterContent {...props} />
        </div>
      </div>

      {/* Mobile Drawer */}
      <div className="lg:hidden">
        <Sheet>
          <SheetTrigger asChild>
            <Button variant="outline" size="sm" className="gap-2 bg-transparent">
              <SlidersHorizontal className="w-4 h-4" />
              Bộ lọc
            </Button>
          </SheetTrigger>
          <SheetContent side="left" className="w-80 overflow-y-auto">
            <SheetHeader>
              <SheetTitle>Bộ lọc</SheetTitle>
            </SheetHeader>
            <div className="mt-6">
              <FilterContent {...props} />
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </>
  )
}
