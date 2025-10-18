import React, { useState } from "react"
import { X } from "lucide-react"

export function SidebarFilter({ categories = [], brands = [], sizes = [], colors = [], priceRange = [0, 0], filters, onChange }) {
  const [localPriceRange, setLocalPriceRange] = useState(filters.priceRange)

  const handleCheckboxChange = (type, id) => {
    const currentValues = filters[type] || []
    const newValues = currentValues.includes(id)
      ? currentValues.filter((item) => item !== id)
      : [...currentValues, id]

    onChange?.({
      ...filters,
      [type]: newValues,
    })
  }

  const handlePriceChange = (index, value) => {
    const newRange = [...localPriceRange]
    newRange[index] = parseInt(value) || 0
    setLocalPriceRange(newRange)
  }

  const applyPriceFilter = () => {
    onChange?.({
      ...filters,
      priceRange: localPriceRange,
    })
  }

  const handleStockOnlyChange = () => {
    onChange?.({
      ...filters,
      stockOnly: !filters.stockOnly,
    })
  }

  const clearFilters = () => {
    setLocalPriceRange(priceRange)
    onChange?.({
      categories: [],
      brands: [],
      sizes: [],
      colors: [],
      priceRange,
      stockOnly: false,
    })
  }

  const formatPrice = (price) => {
    return new Intl.NumberFormat("vi-VN").format(price) + " ₫"
  }

  const hasActiveFilters =
    filters.categories.length > 0 ||
    filters.brands.length > 0 ||
    filters.sizes.length > 0 ||
    filters.colors.length > 0 ||
    filters.priceRange[0] !== priceRange[0] ||
    filters.priceRange[1] !== priceRange[1] ||
    filters.stockOnly

  return (
    <aside className="w-72 hidden lg:block">
      <div className="bg-white rounded-2xl border border-border shadow-sm sticky top-4">
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-border">
          <h2 className="font-semibold text-foreground">Bộ lọc</h2>
          {hasActiveFilters && (
            <button
              type="button"
              onClick={clearFilters}
              className="text-sm text-primary hover:underline flex items-center gap-1"
            >
              <X className="w-4 h-4" />
              Xóa
            </button>
          )}
        </div>

        <div className="max-h-[calc(100vh-120px)] overflow-y-auto">
          {/* Categories */}
          {categories.length > 0 && (
            <div className="p-4 border-b border-border">
              <h3 className="font-medium text-foreground mb-3">Danh mục</h3>
              <div className="space-y-2">
                {categories.map((category) => (
                  <label key={category.id} className="flex items-center gap-2 cursor-pointer group">
                    <input
                      type="checkbox"
                      checked={filters.categories.includes(category.id)}
                      onChange={() => handleCheckboxChange("categories", category.id)}
                      className="w-4 h-4 rounded border-border text-primary focus:ring-2 focus:ring-primary"
                    />
                    <span className="text-sm text-foreground group-hover:text-primary transition-colors">
                      {category.name}
                    </span>
                  </label>
                ))}
              </div>
            </div>
          )}

          {/* Brands */}
          {brands.length > 0 && (
            <div className="p-4 border-b border-border">
              <h3 className="font-medium text-foreground mb-3">Thương hiệu</h3>
              <div className="space-y-2">
                {brands.map((brand) => (
                  <label key={brand.id} className="flex items-center gap-2 cursor-pointer group">
                    <input
                      type="checkbox"
                      checked={filters.brands.includes(brand.id)}
                      onChange={() => handleCheckboxChange("brands", brand.id)}
                      className="w-4 h-4 rounded border-border text-primary focus:ring-2 focus:ring-primary"
                    />
                    <span className="text-sm text-foreground group-hover:text-primary transition-colors">
                      {brand.name}
                    </span>
                  </label>
                ))}
              </div>
            </div>
          )}

          {/* Sizes */}
          {sizes.length > 0 && (
            <div className="p-4 border-b border-border">
              <h3 className="font-medium text-foreground mb-3">Kích thước</h3>
              <div className="flex flex-wrap gap-2">
                {sizes.map((size) => (
                  <button
                    key={size.id}
                    type="button"
                    onClick={() => handleCheckboxChange("sizes", size.id)}
                    className={`px-4 py-2 rounded-lg border text-sm font-medium transition-all ${
                      filters.sizes.includes(size.id)
                        ? "border-primary bg-primary text-white"
                        : "border-border bg-white text-foreground hover:border-primary"
                    }`}
                  >
                    {size.name}
                  </button>
                ))}
              </div>
            </div>
          )}

          {/* Colors */}
          {colors.length > 0 && (
            <div className="p-4 border-b border-border">
              <h3 className="font-medium text-foreground mb-3">Màu sắc</h3>
              <div className="space-y-2">
                {colors.map((color) => (
                  <label key={color.id} className="flex items-center gap-2 cursor-pointer group">
                    <input
                      type="checkbox"
                      checked={filters.colors.includes(color.id)}
                      onChange={() => handleCheckboxChange("colors", color.id)}
                      className="w-4 h-4 rounded border-border text-primary focus:ring-2 focus:ring-primary"
                    />
                    <div className="flex items-center gap-2">
                      {color.hex_code && (
                        <div
                          className="w-5 h-5 rounded border border-border"
                          style={{ backgroundColor: color.hex_code }}
                        />
                      )}
                      <span className="text-sm text-foreground group-hover:text-primary transition-colors">
                        {color.name}
                      </span>
                    </div>
                  </label>
                ))}
              </div>
            </div>
          )}

          {/* Price Range */}
          <div className="p-4 border-b border-border">
            <h3 className="font-medium text-foreground mb-3">Giá</h3>
            <div className="space-y-4">
              <div className="relative h-5 flex items-center">
                {/* Track */}
                <div className="absolute w-full h-2 bg-muted rounded-full">
                  {/* Active range highlight */}
                  <div
                    className="absolute h-2 rounded-full"
                    style={{
                      backgroundColor: "hsl(217.2 91.2% 55%)",
                      left: `${((localPriceRange[0] - priceRange[0]) / (priceRange[1] - priceRange[0])) * 100}%`,
                      right: `${100 - ((localPriceRange[1] - priceRange[0]) / (priceRange[1] - priceRange[0])) * 100}%`,
                    }}
                  />
                </div>

                {/* Min handle */}
                <input
                  type="range"
                  min={priceRange[0]}
                  max={priceRange[1]}
                  step={5000}
                  value={localPriceRange[0]}
                  onChange={(e) => {
                    const value = parseInt(e.target.value)
                    if (value < localPriceRange[1]) {
                      handlePriceChange(0, value)
                    }
                  }}
                  onMouseUp={applyPriceFilter}
                  onTouchEnd={applyPriceFilter}
                  className="absolute w-full appearance-none bg-transparent cursor-pointer z-20 [&::-webkit-slider-thumb]:appearance-none [&::-webkit-slider-thumb]:w-5 [&::-webkit-slider-thumb]:h-5 [&::-webkit-slider-thumb]:rounded-full [&::-webkit-slider-thumb]:bg-white [&::-webkit-slider-thumb]:border-2 [&::-webkit-slider-thumb]:border-primary [&::-webkit-slider-thumb]:cursor-grab [&::-webkit-slider-thumb]:active:cursor-grabbing [&::-webkit-slider-thumb]:shadow-md [&::-webkit-slider-thumb]:hover:scale-110 [&::-webkit-slider-thumb]:transition-transform [&::-moz-range-thumb]:appearance-none [&::-moz-range-thumb]:w-5 [&::-moz-range-thumb]:h-5 [&::-moz-range-thumb]:rounded-full [&::-moz-range-thumb]:bg-white [&::-moz-range-thumb]:border-2 [&::-moz-range-thumb]:border-primary [&::-moz-range-thumb]:cursor-grab [&::-moz-range-thumb]:active:cursor-grabbing [&::-moz-range-thumb]:shadow-md [&::-moz-range-thumb]:hover:scale-110 [&::-moz-range-thumb]:transition-transform [&::-webkit-slider-thumb]:border-none"
                  style={{ pointerEvents: 'all' }}
                />

                {/* Max handle */}
                <input
                  type="range"
                  min={priceRange[0]}
                  max={priceRange[1]}
                  step={5000}
                  value={localPriceRange[1]}
                  onChange={(e) => {
                    const value = parseInt(e.target.value)
                    if (value > localPriceRange[0]) {
                      handlePriceChange(1, value)
                    }
                  }}
                  onMouseUp={applyPriceFilter}
                  onTouchEnd={applyPriceFilter}
                  className="absolute w-full appearance-none bg-transparent cursor-pointer z-30 [&::-webkit-slider-thumb]:appearance-none [&::-webkit-slider-thumb]:w-5 [&::-webkit-slider-thumb]:h-5 [&::-webkit-slider-thumb]:rounded-full [&::-webkit-slider-thumb]:bg-white [&::-webkit-slider-thumb]:border-2 [&::-webkit-slider-thumb]:border-primary [&::-webkit-slider-thumb]:cursor-grab [&::-webkit-slider-thumb]:active:cursor-grabbing [&::-webkit-slider-thumb]:shadow-md [&::-webkit-slider-thumb]:hover:scale-110 [&::-webkit-slider-thumb]:transition-transform [&::-moz-range-thumb]:appearance-none [&::-moz-range-thumb]:w-5 [&::-moz-range-thumb]:h-5 [&::-moz-range-thumb]:rounded-full [&::-moz-range-thumb]:bg-white [&::-moz-range-thumb]:border-2 [&::-moz-range-thumb]:border-primary [&::-moz-range-thumb]:cursor-grab [&::-moz-range-thumb]:active:cursor-grabbing [&::-moz-range-thumb]:shadow-md [&::-moz-range-thumb]:hover:scale-110 [&::-moz-range-thumb]:transition-transform [&::-webkit-slider-thumb]:border-none"
                  style={{ pointerEvents: 'all' }}
                />
              </div>

              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>{formatPrice(localPriceRange[0])}</span>
                <span>{formatPrice(localPriceRange[1])}</span>
              </div>
            </div>
          </div>

          {/* Stock Only */}
          <div className="p-4">
            <label className="flex items-center gap-2 cursor-pointer group">
              <input
                type="checkbox"
                checked={filters.stockOnly}
                onChange={handleStockOnlyChange}
                className="w-4 h-4 rounded border-border text-primary focus:ring-2 focus:ring-primary"
              />
              <span className="text-sm text-foreground group-hover:text-primary transition-colors">
                Chỉ hiển thị còn hàng
              </span>
            </label>
          </div>
        </div>
      </div>
    </aside>
  )
}

export default SidebarFilter
