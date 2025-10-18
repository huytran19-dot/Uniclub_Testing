"use client"

import { Menu, Bell, User } from "lucide-react"
import { useState } from "react"

export default function Topbar({ onMenuClick }) {
  const [showUserMenu, setShowUserMenu] = useState(false)

  return (
    <header className="bg-white border-b border-neutral-200 shadow-sm">
      <div className="flex items-center justify-between px-6 py-4">
        <button onClick={onMenuClick} className="md:hidden p-2 hover:bg-neutral-100 rounded-lg">
          <Menu size={20} />
        </button>

        <div className="flex-1 max-w-md mx-4">
          <input
            type="text"
            placeholder="Tìm kiếm..."
            className="w-full px-4 py-2 border border-neutral-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="flex items-center gap-4">
          <button className="p-2 hover:bg-neutral-100 rounded-lg relative">
            <Bell size={20} />
            <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full" />
          </button>

          <div className="relative">
            <button onClick={() => setShowUserMenu(!showUserMenu)} className="p-2 hover:bg-neutral-100 rounded-lg">
              <User size={20} />
            </button>
            {showUserMenu && (
              <div className="absolute right-0 mt-2 w-48 bg-white border border-neutral-200 rounded-lg shadow-lg z-50">
                <button className="w-full text-left px-4 py-2 hover:bg-neutral-100">Hồ sơ</button>
                <button className="w-full text-left px-4 py-2 hover:bg-neutral-100">Đăng xuất</button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  )
}
