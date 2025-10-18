"use client"

import { useEffect, useState, useMemo, useRef } from "react"
import { useSearchParams, useNavigate } from "react-router-dom"
import { Search, Grid3x3, List } from "lucide-react"
import { PageLayout } from "../components/PageLayout"
import { SidebarFilter } from "../components/SidebarFilter"
import { ProductCard } from "../components/ProductCard"
import { Button } from "../components/ui/button"
import { Input } from "../components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select"
import { products, variants, brands, categories, sizes, colors, reviews } from "../lib/mock-data"
import { calculateAverageRating, getMinPrice, isOutOfStock } from "../lib/utils"
import { addToCart } from "../lib/cart"

export default function ProductsPage() {
  const [searchParams, setSearchParams] = useSearchParams()
  const navigate = useNavigate()
  const [mounted, setMounted] = useState(false)
  const initializedFromURL = useRef(false)
  const [searchQuery, setSearchQuery] = useState("")
  const [viewMode, setViewMode] = useState("grid")
  const [sortBy, setSortBy] = useState("newest")

  const priceRange = useMemo(() => {
    const prices = variants.filter((v) => v.status === 1 && v.price !== null).map((v) => v.price)
    return [Math.min(...prices), Math.max(...prices)]
  }, [])

  const [filters, setFilters] = useState({
    categories: [],
    brands: [],
    sizes: [],
    colors: [],
    priceRange,
    stockOnly: false,
  })

  useEffect(() => {
    if (initializedFromURL.current) return

    setMounted(true)
    initializedFromURL.current = true

    const categoryParam = searchParams.get("category")
    const brandParam = searchParams.get("brand")
    const searchParam = searchParams.get("search")
    const sortParam = searchParams.get("sort")

    if (categoryParam) {
      setFilters((prev) => ({
        ...prev,
        categories: categoryParam.split(",").map(Number),
      }))
    }
    if (brandParam) {
      setFilters((prev) => ({
        ...prev,
        brands: brandParam.split(",").map(Number),
      }))
    }
    if (searchParam) {
      setSearchQuery(searchParam)
    }
    if (sortParam) {
      setSortBy(sortParam)
    }
  }, [searchParams])

  const updateURL = (newFilters, search, sort) => {
    const params = new URLSearchParams()
    if (newFilters.categories.length) params.set("category", newFilters.categories.join(","))
    if (newFilters.brands.length) params.set("brand", newFilters.brands.join(","))
    if (newFilters.sizes.length) params.set("size", newFilters.sizes.join(","))
    if (newFilters.colors.length) params.set("color", newFilters.colors.join(","))
    if (newFilters.priceRange[0] !== priceRange[0]) params.set("priceMin", newFilters.priceRange[0].toString())
    if (newFilters.priceRange[1] !== priceRange[1]) params.set("priceMax", newFilters.priceRange[1].toString())
    if (newFilters.stockOnly) params.set("stock", "1")
    if (search) params.set("search", search)
    if (sort !== "newest") params.set("sort", sort)

    setSearchParams(params)
  }

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters)
    updateURL(newFilters, searchQuery, sortBy)
  }

  const handleSearch = (query) => {
    setSearchQuery(query)
    updateURL(filters, query, sortBy)
  }

  const handleSortChange = (sort) => {
    setSortBy(sort)
    updateURL(filters, searchQuery, sort)
  }

  const filteredProducts = useMemo(() => {
    let filtered = products.filter((p) => p.status === 1)

    if (searchQuery) {
      filtered = filtered.filter((p) => p.name.toLowerCase().includes(searchQuery.toLowerCase()))
    }

    if (filters.categories.length > 0) {
      filtered = filtered.filter((p) => filters.categories.includes(p.id_category))
    }

    if (filters.brands.length > 0) {
      filtered = filtered.filter((p) => filters.brands.includes(p.id_brand))
    }

    filtered = filtered.filter((product) => {
      const productVariants = variants.filter((v) => v.id_product === product.id && v.status === 1)

      if (filters.sizes.length > 0) {
        const hasSizes = productVariants.some((v) => filters.sizes.includes(v.id_size))
        if (!hasSizes) return false
      }

      if (filters.colors.length > 0) {
        const hasColors = productVariants.some((v) => filters.colors.includes(v.id_color))
        if (!hasColors) return false
      }

      const minPrice = getMinPrice(productVariants, product.price)
      if (minPrice < filters.priceRange[0] || minPrice > filters.priceRange[1]) {
        return false
      }

      if (filters.stockOnly && isOutOfStock(productVariants)) {
        return false
      }

      return true
    })

    filtered.sort((a, b) => {
      if (sortBy === "newest") {
        return new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
      }
      if (sortBy === "price-asc") {
        const aVariants = variants.filter((v) => v.id_product === a.id && v.status === 1)
        const bVariants = variants.filter((v) => v.id_product === b.id && v.status === 1)
        return getMinPrice(aVariants, a.price) - getMinPrice(bVariants, b.price)
      }
      if (sortBy === "price-desc") {
        const aVariants = variants.filter((v) => v.id_product === a.id && v.status === 1)
        const bVariants = variants.filter((v) => v.id_product === b.id && v.status === 1)
        return getMinPrice(bVariants, b.price) - getMinPrice(aVariants, a.price)
      }
      if (sortBy === "rating") {
        const aReviews = reviews.filter((r) => r.id_product === a.id && r.status === 1)
        const bReviews = reviews.filter((r) => r.id_product === b.id && r.status === 1)
        return calculateAverageRating(bReviews) - calculateAverageRating(aReviews)
      }
      return 0
    })

    return filtered
  }, [filters, searchQuery, sortBy])

  const handleAddToCart = (product) => {
    const productVariants = variants.filter((v) => v.id_product === product.id && v.status === 1)
    const availableVariant = productVariants.find((v) => v.quantity > 0)

    if (!availableVariant) return

    const size = sizes.find((s) => s.id === availableVariant.id_size)
    const color = colors.find((c) => c.id === availableVariant.id_color)

    addToCart({
      sku_variant: availableVariant.sku,
      productName: product.name,
      sizeName: size?.name || "N/A",
      colorName: color?.name || "N/A",
      unitPrice: availableVariant.price || product.price,
      image: availableVariant.images,
      maxQuantity: availableVariant.quantity,
    })

    window.dispatchEvent(new Event("cart-updated"))
  }

  if (!mounted) return null

  const activeCategories = categories.filter((c) => c.status === 1)
  const activeBrands = brands.filter((b) => b.status === 1)
  const activeSizes = sizes.filter((s) => s.status === 1)
  const activeColors = colors.filter((c) => c.status === 1)

  return (
    <PageLayout title="Sản phẩm" breadcrumbs={[{ label: "Sản phẩm" }]}>
      <div className="flex gap-8">
        <SidebarFilter
          categories={activeCategories}
          brands={activeBrands}
          sizes={activeSizes}
          colors={activeColors}
          priceRange={priceRange}
          filters={filters}
          onChange={handleFilterChange}
        />

        <div className="flex-1 min-w-0">
          <div className="bg-white rounded-2xl border border-border p-4 mb-6 shadow-sm">
            <div className="flex flex-col md:flex-row gap-4">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder="Tìm kiếm sản phẩm..."
                  value={searchQuery}
                  onChange={(e) => handleSearch(e.target.value)}
                  className="pl-10"
                />
              </div>

              <Select value={sortBy} onValueChange={(value) => handleSortChange(value)}>
                <SelectTrigger className="w-full md:w-48">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="newest">Mới nhất</SelectItem>
                  <SelectItem value="price-asc">Giá tăng dần</SelectItem>
                  <SelectItem value="price-desc">Giá giảm dần</SelectItem>
                  <SelectItem value="rating">Đánh giá cao</SelectItem>
                </SelectContent>
              </Select>

              <div className="hidden md:flex gap-2">
                <Button
                  variant={viewMode === "grid" ? "default" : "outline"}
                  size="icon"
                  onClick={() => setViewMode("grid")}
                >
                  <Grid3x3 className="w-4 h-4" />
                </Button>
                <Button
                  variant={viewMode === "list" ? "default" : "outline"}
                  size="icon"
                  onClick={() => setViewMode("list")}
                >
                  <List className="w-4 h-4" />
                </Button>
              </div>
            </div>
          </div>

          <div className="mb-4 text-sm text-muted-foreground">Tìm thấy {filteredProducts.length} sản phẩm</div>

          {filteredProducts.length === 0 ? (
            <div className="text-center py-16">
              <p className="text-lg text-muted-foreground mb-4">Không tìm thấy sản phẩm</p>
              <Button
                variant="outline"
                onClick={() =>
                  handleFilterChange({
                    categories: [],
                    brands: [],
                    sizes: [],
                    colors: [],
                    priceRange,
                    stockOnly: false,
                  })
                }
              >
                Xóa bộ lọc
              </Button>
            </div>
          ) : (
            <div
              className={
                viewMode === "grid" ? "grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6" : "flex flex-col gap-4"
              }
            >
              {filteredProducts.map((product) => {
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
                    onAddToCart={() => handleAddToCart(product)}
                  />
                )
              })}
            </div>
          )}
        </div>
      </div>
    </PageLayout>
  )
}
