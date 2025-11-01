"use client"

import { Link, useLocation } from "react-router-dom"
import { ChevronDown } from "lucide-react"
import { useState } from "react"

const menuItems = [
  { label: "Tổng quan", path: "/dashboard", icon: "📊" },
  {
    label: "Sản phẩm",
    icon: "📦",
    submenu: [
      { label: "Danh mục", path: "/categories" },
      { label: "Nhãn hàng", path: "/brands" },
      { label: "Kích cỡ", path: "/sizes" },
      { label: "Màu sắc", path: "/colors" },
      { label: "Sản phẩm", path: "/products" },
      { label: "Biến thể", path: "/variants" },
    ],
  },
  { label: "Đơn hàng", path: "/orders", icon: "🛒" },
  { label: "Nhà cung cấp", path: "/suppliers", icon: "🏭" },
  { label: "Phiếu nhập", path: "/grn", icon: "📋" },
  { label: "Người dùng", path: "/users", icon: "👥" },
]

export default function Sidebar({ open, onToggle }) {
  const location = useLocation()
  const [expandedMenu, setExpandedMenu] = useState(null)

  const isActive = (path) => location.pathname.startsWith(path)

  return (
    <>
      {/* Mobile overlay */}
      {open && <div className="fixed inset-0 bg-black/50 z-30 md:hidden" onClick={onToggle} />}

      {/* Sidebar */}
      <aside
        className={`fixed md:relative w-64 h-screen bg-white border-r border-neutral-200 shadow-sm transition-transform duration-300 z-40 ${
          open ? "translate-x-0" : "-translate-x-full md:translate-x-0"
        }`}
      >
        <div className="p-6 border-b border-neutral-200">
          <h1 className="text-2xl font-bold text-blue-600">UniClub</h1>
          <p className="text-sm text-neutral-500">Admin Dashboard</p>
        </div>

        <nav className="p-4 space-y-2 overflow-y-auto h-[calc(100vh-120px)]">
          {menuItems.map((item) => (
            <div key={item.label}>
              {item.submenu ? (
                <div>
                  <button
                    onClick={() => setExpandedMenu(expandedMenu === item.label ? null : item.label)}
                    className={`w-full flex items-center justify-between px-4 py-2 rounded-lg transition-colors ${
                      expandedMenu === item.label ? "bg-blue-50 text-blue-600" : "text-neutral-700 hover:bg-neutral-100"
                    }`}
                  >
                    <span className="flex items-center gap-3">
                      <span>{item.icon}</span>
                      {item.label}
                    </span>
                    <ChevronDown
                      size={16}
                      className={`transition-transform ${expandedMenu === item.label ? "rotate-180" : ""}`}
                    />
                  </button>
                  {expandedMenu === item.label && (
                    <div className="ml-4 mt-2 space-y-1">
                      {item.submenu.map((subitem) => (
                        <Link
                          key={subitem.path}
                          to={subitem.path}
                          className={`block px-4 py-2 rounded-lg text-sm transition-colors ${
                            isActive(subitem.path)
                              ? "bg-blue-100 text-blue-600 font-medium"
                              : "text-neutral-600 hover:bg-neutral-100"
                          }`}
                        >
                          {subitem.label}
                        </Link>
                      ))}
                    </div>
                  )}
                </div>
              ) : (
                <Link
                  to={item.path}
                  className={`flex items-center gap-3 px-4 py-2 rounded-lg transition-colors ${
                    isActive(item.path)
                      ? "bg-blue-100 text-blue-600 font-medium"
                      : "text-neutral-700 hover:bg-neutral-100"
                  }`}
                >
                  <span>{item.icon}</span>
                  {item.label}
                </Link>
              )}
            </div>
          ))}
        </nav>
      </aside>
    </>
  )
}
