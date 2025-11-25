import React, { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import { getCategories } from "@/lib/api"

export default function FeaturedCategories() {
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getCategories()
      .then(data => {
        const activeCategories = data.filter(c => c.status === 1)
        setCategories(activeCategories)
      })
      .catch(error => {
        console.error("Failed to fetch categories:", error)
      })
      .finally(() => {
        setLoading(false)
      })
  }, [])

  if (loading) {
    return (
      <section className="section">
        <h2 className="text-2xl font-semibold tracking-tight text-foreground mb-4">Danh mục nổi bật</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {[...Array(3)].map((_, i) => (
            <div key={i} className="card animate-pulse">
              <div className="aspect-[16/10] bg-muted rounded-lg"></div>
            </div>
          ))}
        </div>
      </section>
    )
  }

  return (
    <section className="section">
      <div className="flex items-baseline justify-between mb-4">
        <h2 className="text-2xl font-semibold tracking-tight text-foreground">Danh mục nổi bật</h2>
        <Link to="/products" className="link-underline text-sm">Xem tất cả</Link>
      </div>
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
        {categories.map((cat) => (
          <Link 
            key={cat.id} 
            to={`/products?category=${cat.id}`} 
            className="group"
          >
            <div className="relative overflow-hidden rounded-xl card shadow-soft hover:shadow-lg transition-all duration-300 h-32 flex items-center justify-center bg-gradient-to-br from-primary/10 to-primary/5 hover:from-primary/20 hover:to-primary/10">
              <div className="text-center p-4">
                <span className="text-foreground font-semibold text-lg group-hover:text-primary transition-colors">
                  {cat.name}
                </span>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </section>
  )
}
