import React from "react"
import { Link } from "react-router-dom"
import { categories } from "@/lib/mock-data"

const categoryImages = {
  1: "/black-basic-tshirt.png",
  2: "/red-women-hoodie.jpg",
  3: "/black-street-cap.jpg",
}

export default function FeaturedCategories() {
  const featured = categories.filter((c) => c.status === 1)

  return (
    <section className="section">
      <div className="flex items-baseline justify-between mb-4">
        <h2 className="text-2xl font-semibold tracking-tight text-foreground">Danh mục nổi bật</h2>
        <Link to="/products" className="link-underline text-sm">Xem tất cả</Link>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {featured.map((cat) => (
          <Link key={cat.id} to={`/products?category=${cat.id}`} className="group">
            <div className="relative overflow-hidden rounded-2xl card shadow-soft">
              <div className="aspect-[16/10] bg-secondary">
                <img
                  src={categoryImages[cat.id] || "/white-basic-tshirt.png"}
                  alt={cat.name}
                  className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
                />
              </div>
              <div className="absolute bottom-0 left-0 right-0 p-4 flex items-center justify-between bg-gradient-to-t from-black/40 to-transparent">
                <span className="text-white font-medium text-lg">{cat.name}</span>
                <span className="text-white/90 text-sm group-hover:underline">Khám phá</span>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </section>
  )
}
