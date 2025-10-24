"use client"

import { Menu, Bell, User } from "lucide-react"
import { useState } from "react"
import { useAuth } from "../lib/auth.jsx"
import { useNavigate } from "react-router-dom"

export default function Topbar({ onMenuClick }) {
  const [showUserMenu, setShowUserMenu] = useState(false)
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

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
                <div className="px-4 py-2 border-b border-neutral-200">
                  <p className="text-sm font-medium text-gray-900">{user?.fullName}</p>
                  <p className="text-xs text-gray-500">{user?.email}</p>
                </div>
                <button className="w-full text-left px-4 py-2 hover:bg-neutral-100">Hồ sơ</button>
                <button 
                  onClick={handleLogout}
                  className="w-full text-left px-4 py-2 hover:bg-neutral-100 text-red-600"
                >
                  Đăng xuất
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  )
}
