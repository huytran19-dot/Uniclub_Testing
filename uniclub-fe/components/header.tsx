"use client"

import { ShoppingCart, Search, User, Menu } from "lucide-react"
import Link from "next/link"
import { useState, useEffect } from "react"
import { categories } from "@/lib/mock-data"
import { getCartCount } from "@/lib/cart"
import { getCurrentUser, logout } from "@/lib/auth"
import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { useRouter } from "next/navigation"

export function Header() {
  const [cartCount, setCartCount] = useState(0)
  const [user, setUser] = useState<ReturnType<typeof getCurrentUser>>(null)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const router = useRouter()

  useEffect(() => {
    setCartCount(getCartCount())
    setUser(getCurrentUser())

    const handleStorage = () => {
      setCartCount(getCartCount())
      setUser(getCurrentUser())
    }

    window.addEventListener("storage", handleStorage)
    window.addEventListener("cart-updated", handleStorage)
    window.addEventListener("auth-updated", handleStorage)

    return () => {
      window.removeEventListener("storage", handleStorage)
      window.removeEventListener("cart-updated", handleStorage)
      window.removeEventListener("auth-updated", handleStorage)
    }
  }, [])

  const handleLogout = () => {
    logout()
    setUser(null)
    window.dispatchEvent(new Event("auth-updated"))
    router.push("/")
  }

  const activeCategories = categories.filter((c) => c.status === 1)

  return (
    <header className="sticky top-0 z-50 bg-background border-b border-border shadow-sm">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link href="/" className="flex items-center gap-2">
            <div className="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-xl">U</span>
            </div>
            <span className="text-xl font-bold text-foreground">UniClub</span>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center gap-6">
            {activeCategories.map((category) => (
              <Link
                key={category.id}
                href={`/products?category=${category.id}`}
                className="text-sm font-medium text-foreground hover:text-primary transition-colors"
              >
                {category.name}
              </Link>
            ))}
          </nav>

          {/* Actions */}
          <div className="flex items-center gap-4">
            <Button variant="ghost" size="icon" onClick={() => router.push("/products")} className="hidden md:flex">
              <Search className="w-5 h-5" />
              <span className="sr-only">Tìm kiếm</span>
            </Button>

            <Link href="/cart">
              <Button variant="ghost" size="icon" className="relative">
                <ShoppingCart className="w-5 h-5" />
                {cartCount > 0 && (
                  <span className="absolute -top-1 -right-1 bg-danger text-white text-xs w-5 h-5 rounded-full flex items-center justify-center">
                    {cartCount}
                  </span>
                )}
                <span className="sr-only">Giỏ hàng</span>
              </Button>
            </Link>

            {user ? (
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" size="icon">
                    <User className="w-5 h-5" />
                    <span className="sr-only">Tài khoản</span>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-48">
                  <div className="px-2 py-1.5 text-sm font-medium">{user.full_name}</div>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem onClick={() => router.push("/orders")}>Đơn hàng của tôi</DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem onClick={handleLogout}>Đăng xuất</DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            ) : (
              <Link href="/login">
                <Button variant="ghost" size="icon">
                  <User className="w-5 h-5" />
                  <span className="sr-only">Đăng nhập</span>
                </Button>
              </Link>
            )}

            <Button
              variant="ghost"
              size="icon"
              className="md:hidden"
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            >
              <Menu className="w-5 h-5" />
              <span className="sr-only">Menu</span>
            </Button>
          </div>
        </div>

        {/* Mobile Menu */}
        {mobileMenuOpen && (
          <div className="md:hidden border-t border-border py-4">
            <nav className="flex flex-col gap-4">
              {activeCategories.map((category) => (
                <Link
                  key={category.id}
                  href={`/products?category=${category.id}`}
                  className="text-sm font-medium text-foreground hover:text-primary transition-colors"
                  onClick={() => setMobileMenuOpen(false)}
                >
                  {category.name}
                </Link>
              ))}
            </nav>
          </div>
        )}
      </div>
    </header>
  )
}
