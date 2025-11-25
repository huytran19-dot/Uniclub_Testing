"use client"

import { ShoppingCart, Search, User, Menu } from "lucide-react"
import { Link, useNavigate } from "react-router-dom"
import { useState, useEffect } from "react"
import { getCategories } from "@/lib/api"
import { getUserCart, getCartItems } from "@/lib/cart-api"
import { getCurrentUser, logout } from "@/lib/auth"
import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

export function Header() {
  const [cartCount, setCartCount] = useState(0)
  const [user, setUser] = useState(null)
  const [categories, setCategories] = useState([])
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const navigate = useNavigate()

  const fetchCartCount = async () => {
    const userStr = localStorage.getItem('uniclub_user')
    if (!userStr) {
      setCartCount(0)
      return
    }

    try {
      const user = JSON.parse(userStr)
      const cart = await getUserCart(user.id)
      const items = await getCartItems(cart.id)
      setCartCount(items.length)
    } catch (error) {
      setCartCount(0)
    }
  }

  useEffect(() => {
    fetchCartCount()
    setUser(getCurrentUser())

    // Fetch categories from API
    getCategories()
      .then(data => {
        // Filter active categories (status === 1)
        const activeCategories = data.filter(c => c.status === 1)
        setCategories(activeCategories)
      })
      .catch(error => {
        console.error("Failed to fetch categories:", error)
        setCategories([])
      })

    const handleUpdate = () => {
      fetchCartCount()
      setUser(getCurrentUser())
    }

    window.addEventListener("cart-updated", handleUpdate)
    window.addEventListener("auth-updated", handleUpdate)

    return () => {
      window.removeEventListener("cart-updated", handleUpdate)
      window.removeEventListener("auth-updated", handleUpdate)
    }
  }, [])

  const handleLogout = () => {
    logout()
    setUser(null)
    window.dispatchEvent(new Event("auth-updated"))
    navigate("/")
  }

  return (
    <header className="sticky top-0 z-50 border-b border-border shadow-sm bg-card/80 backdrop-blur supports-[backdrop-filter]:bg-card/70">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          <Link to="/" className="flex items-center gap-2">
            <div className="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-xl">U</span>
            </div>
            <span className="text-xl font-bold text-foreground">UniClub</span>
          </Link>

          <nav className="hidden md:flex items-center gap-6">
            {categories.map((category) => (
              <Link
                key={category.id}
                to={`/products?category=${category.id}`}
                className="text-sm font-medium text-foreground hover:text-primary transition-colors underline-offset-4 decoration-2 decoration-transparent hover:decoration-primary"
              >
                {category.name}
              </Link>
            ))}
          </nav>

          <div className="flex items-center gap-4">
            <Button variant="ghost" size="icon" onClick={() => navigate("/products")} className="hidden md:flex">
              <Search className="w-5 h-5" />
              <span className="sr-only">Tìm kiếm</span>
            </Button>

            <Link to="/cart">
              <Button variant="ghost" size="icon" className="relative">
                <ShoppingCart className="w-5 h-5" />
                {cartCount > 0 && (
                  <span className="absolute -top-1 -right-1 bg-destructive text-white text-xs w-5 h-5 rounded-full flex items-center justify-center">
                    {cartCount}
                  </span>
                )}
                <span className="sr-only">Giỏ hàng</span>
              </Button>
            </Link>

            {user ? (
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <button className="flex items-center justify-center w-10 h-10 rounded-full text-white font-semibold text-base hover:opacity-90 transition-opacity" style={{ backgroundColor: "hsl(217.2 91.2% 55%)" }}>
                    {user.full_name?.charAt(0).toUpperCase() || user.email?.charAt(0).toUpperCase() || "U"}
                  </button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-56 p-0">
                  <div className="px-4 py-3 bg-muted">
                    <div className="font-medium text-foreground">Hi, {user.full_name || user.email?.split('@')[0]}</div>
                  </div>
                  <div className="py-1">
                    <DropdownMenuItem onClick={() => navigate("/profile")} className="cursor-pointer px-4 py-2.5">
                      Xem thông tin chi tiết
                    </DropdownMenuItem>
                    <DropdownMenuItem onClick={handleLogout} className="cursor-pointer px-4 py-2.5">
                      Đăng xuất
                    </DropdownMenuItem>
                  </div>
                </DropdownMenuContent>
              </DropdownMenu>
            ) : (
              <Link to="/login">
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

        {mobileMenuOpen && (
          <div className="md:hidden border-t border-border py-4">
            <nav className="flex flex-col gap-4">
              {activeCategories.map((category) => (
                <Link
                  key={category.id}
                  to={`/products?category=${category.id}`}
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
