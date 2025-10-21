"use client"

import { ChevronLeft, ChevronRight } from "lucide-react"
import { useState } from "react"
import { ActionButtonGroup, ActionDropdown } from "./ActionButtons"

export default function Table({
  columns,
  data = [], // ← Add default value
  onEdit,
  onDelete,
  onView,
  onVariants,
  customActions,
  searchable = true,
  filterable = true,
  sortable = true,
}) {
  const [currentPage, setCurrentPage] = useState(1)
  const [searchTerm, setSearchTerm] = useState("")
  const [sortConfig, setSortConfig] = useState(null)
  const [loadingRow, setLoadingRow] = useState(null)
  const itemsPerPage = 10

  // Ensure data is always an array
  const safeData = Array.isArray(data) ? data : []
  const filteredData = safeData.filter((item) => JSON.stringify(item).toLowerCase().includes(searchTerm.toLowerCase()))

  if (sortConfig) {
    filteredData.sort((a, b) => {
      const aVal = a[sortConfig.key]
      const bVal = b[sortConfig.key]
      if (aVal < bVal) return sortConfig.direction === "asc" ? -1 : 1
      if (aVal > bVal) return sortConfig.direction === "asc" ? 1 : -1
      return 0
    })
  }

  const totalPages = Math.ceil(filteredData.length / itemsPerPage)
  const startIdx = (currentPage - 1) * itemsPerPage
  const paginatedData = filteredData.slice(startIdx, startIdx + itemsPerPage)

  const handleSort = (key) => {
    if (sortConfig?.key === key) {
      setSortConfig({
        key,
        direction: sortConfig.direction === "asc" ? "desc" : "asc",
      })
    } else {
      setSortConfig({ key, direction: "asc" })
    }
  }

  return (
    <div className="space-y-4">
      {searchable && (
        <input
          type="text"
          placeholder="Tìm kiếm..."
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value)
            setCurrentPage(1)
          }}
          className="w-full px-4 py-2 border border-neutral-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      )}

      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-neutral-200 bg-neutral-50">
              {columns.map((col) => (
                <th
                  key={col.key}
                  className="px-4 py-3 text-left font-medium text-neutral-700 cursor-pointer hover:bg-neutral-100"
                  onClick={() => sortable && handleSort(col.key)}
                >
                  {col.label}
                </th>
              ))}
              {(onEdit || onDelete || onView || onVariants) && (
                <th className="px-4 py-3 text-left font-medium text-neutral-700">Thao tác</th>
              )}
            </tr>
          </thead>
          <tbody>
            {paginatedData.map((row, idx) => (
              <tr key={idx} className="border-b border-neutral-200 hover:bg-neutral-50">
                {columns.map((col) => (
                  <td key={col.key} className="px-4 py-3">
                    {col.render ? col.render(row) : row[col.key]}
                  </td>
                ))}
                {(onEdit || onDelete || onView || onVariants || customActions) && (
                  <td className="px-4 py-3">
                    <div className="flex items-center gap-2">
                      {/* Desktop: Button group */}
                      <div className="hidden sm:block">
                        <ActionButtonGroup
                          row={row}
                          onView={onView}
                          onEdit={onEdit}
                          onDelete={onDelete}
                          onVariants={onVariants}
                          loading={loadingRow === idx ? 'loading' : null}
                        />
                      </div>
                      
                      {/* Mobile: Dropdown */}
                      <div className="sm:hidden">
                        <ActionDropdown
                          row={row}
                          onView={onView}
                          onEdit={onEdit}
                          onDelete={onDelete}
                          onVariants={onVariants}
                        />
                      </div>
                      
                      {customActions && customActions(row)}
                    </div>
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="flex items-center justify-between">
          <span className="text-sm text-neutral-600">
            Trang {currentPage} / {totalPages}
          </span>
          <div className="flex gap-2">
            <button
              onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
              disabled={currentPage === 1}
              className="p-2 border border-neutral-200 rounded-lg disabled:opacity-50"
            >
              <ChevronLeft size={16} />
            </button>
            <button
              onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
              disabled={currentPage === totalPages}
              className="p-2 border border-neutral-200 rounded-lg disabled:opacity-50"
            >
              <ChevronRight size={16} />
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
