"use client"

import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { ChevronLeft, ChevronRight } from "lucide-react"
import Card from "../../components/Card"
import Badge from "../../components/Badge"
import Breadcrumb from "../../components/Breadcrumb"
import { api } from "../../lib/api"
import { formatDateTime, formatMoney } from "../../lib/utils"

export default function ProductVariants() {
  const navigate = useNavigate()
  const { productId } = useParams()
  const [product, setProduct] = useState(null)
  const [variants, setVariants] = useState([])
  const [filteredVariants, setFilteredVariants] = useState([])
  const [searchTerm, setSearchTerm] = useState("")
  const [currentPage, setCurrentPage] = useState(1)
  const itemsPerPage = 10

  useEffect(() => {
    loadData()
  }, [productId])

  useEffect(() => {
    filterVariants()
  }, [searchTerm, variants])

  const loadData = async () => {
    // Try to fetch product
    const prod = await api.get("products", productId)
    if (prod) {
      setProduct(prod)
    } else {
      setProduct({ id: productId, name: "Sản phẩm #" + productId })
    }

    // Fetch all variants and filter by product ID
    const allVariants = await api.list("variants")
    
    if (allVariants && allVariants.length > 0) {
      // Filter variants for this specific product
      const productVariants = allVariants.filter((v) => v.productId === Number.parseInt(productId))
      console.log('Filtered variants for product:', productVariants)
      
      // Fetch colors and sizes for display
      const [colors, sizes] = await Promise.all([api.list("colors"), api.list("sizes")])
      
      // Enrich variant data with color and size names
      const enrichedVariants = productVariants.map((v) => ({
        id: v.sku,
        sku: v.sku,
        color: colors.find((c) => c.id === v.colorId)?.name || "N/A",
        size: sizes.find((s) => s.id === v.sizeId)?.name || "N/A",
        price: v.price,
        stock: v.quantity || 0,
        status: v.status === 1 ? "active" : "inactive",
        updatedAt: v.createdAt,
      }))
      
      setVariants(enrichedVariants)
    }
  }

  const filterVariants = () => {
    const filtered = variants.filter((variant) => {
      const searchLower = searchTerm.toLowerCase()
      return (
        String(variant.sku).toLowerCase().includes(searchLower) ||
        variant.color.toLowerCase().includes(searchLower) ||
        variant.size.toLowerCase().includes(searchLower)
      )
    })
    setFilteredVariants(filtered)
    setCurrentPage(1)
  }

  const totalPages = Math.ceil(filteredVariants.length / itemsPerPage)
  const startIdx = (currentPage - 1) * itemsPerPage
  const paginatedVariants = filteredVariants.slice(startIdx, startIdx + itemsPerPage)

  if (!product) return <div>Đang tải...</div>

  return (
    <div>
      <Breadcrumb
        items={[
          { label: "Sản phẩm", path: "/products" },
          { label: product.name, path: `/products/${productId}` },
          { label: "Biến thể" },
        ]}
      />

      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">Biến thể - {product.name}</h1>
        </div>
        <div className="flex gap-2">
          <button
            onClick={() => navigate(`/variants/new?productId=${productId}`)}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Thêm biến thể mới
          </button>
          <button
            onClick={() => navigate("/products")}
            className="px-4 py-2 border border-neutral-200 rounded-lg hover:bg-neutral-50"
          >
            Quay lại
          </button>
        </div>
      </div>

      <Card>
        <div className="p-6 space-y-4">
          <input
            type="text"
            placeholder="Tìm kiếm theo SKU, màu sắc, hoặc size..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full px-4 py-2 border border-neutral-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-neutral-200 bg-neutral-50">
                  <th className="px-4 py-3 text-left font-medium text-neutral-700">SKU</th>
                  <th className="px-4 py-3 text-left font-medium text-neutral-700">Màu</th>
                  <th className="px-4 py-3 text-left font-medium text-neutral-700">Size</th>
                  <th className="px-4 py-3 text-right font-medium text-neutral-700">Giá</th>
                  <th className="px-4 py-3 text-right font-medium text-neutral-700">Tồn kho</th>
                  <th className="px-4 py-3 text-left font-medium text-neutral-700">Trạng thái</th>
                  <th className="px-4 py-3 text-left font-medium text-neutral-700">Cập nhật</th>
                </tr>
              </thead>
              <tbody>
                {paginatedVariants.length === 0 ? (
                  <tr>
                    <td colSpan="7" className="px-4 py-8 text-center text-neutral-500">
                      Không tìm thấy biến thể nào
                    </td>
                  </tr>
                ) : (
                  paginatedVariants.map((variant) => (
                    <tr key={variant.id} className="border-b border-neutral-200 hover:bg-neutral-50">
                      <td className="px-4 py-3 font-mono text-xs">{variant.sku}</td>
                      <td className="px-4 py-3">{variant.color}</td>
                      <td className="px-4 py-3">{variant.size}</td>
                      <td className="px-4 py-3 text-right font-medium">{formatMoney(variant.price)}</td>
                      <td className="px-4 py-3 text-right">
                        <span className={variant.stock === 0 ? "text-red-600 font-medium" : ""}>
                          {variant.stock}
                        </span>
                      </td>
                      <td className="px-4 py-3">
                        <Badge
                          status={variant.status === "active" ? "success" : "danger"}
                          label={variant.status === "active" ? "Hoạt động" : "Ngừng"}
                        />
                      </td>
                      <td className="px-4 py-3">{formatDateTime(variant.updatedAt)}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          {totalPages > 1 && (
            <div className="flex items-center justify-between pt-4">
              <span className="text-sm text-neutral-600">
                Trang {currentPage} / {totalPages} (Tổng: {filteredVariants.length} biến thể)
              </span>
              <div className="flex gap-2">
                <button
                  onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                  disabled={currentPage === 1}
                  className="p-2 border border-neutral-200 rounded-lg disabled:opacity-50 hover:bg-neutral-50"
                >
                  <ChevronLeft size={16} />
                </button>
                <button
                  onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                  disabled={currentPage === totalPages}
                  className="p-2 border border-neutral-200 rounded-lg disabled:opacity-50 hover:bg-neutral-50"
                >
                  <ChevronRight size={16} />
                </button>
              </div>
            </div>
          )}
        </div>
      </Card>
    </div>
  )
}
