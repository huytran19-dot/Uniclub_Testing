import { ChevronRight } from "lucide-react"
import { Link } from "react-router-dom"
import { Header } from "./Header"
import { Footer } from "./Footer"

export function PageLayout({ title, breadcrumbs = [], children }) {
  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Header />

      {breadcrumbs.length > 0 && (
        <div className="bg-card border-b border-border">
          <div className="container mx-auto px-4 py-3">
            <nav className="flex items-center gap-2 text-sm text-muted-foreground">
              <Link to="/" className="hover:text-foreground transition-colors">
                Trang chủ
              </Link>
              {breadcrumbs.map((crumb, index) => (
                <div key={index} className="flex items-center gap-2">
                  <ChevronRight className="w-4 h-4" />
                  {crumb.href ? (
                    <Link to={crumb.href} className="hover:text-foreground transition-colors">
                      {crumb.label}
                    </Link>
                  ) : (
                    <span className="text-foreground">{crumb.label}</span>
                  )}
                </div>
              ))}
            </nav>
          </div>
        </div>
      )}

      <div className="flex-1">
        <div className="container mx-auto px-4 py-8">
          {title && <h1 className="text-3xl font-bold text-foreground mb-8">{title}</h1>}
          {children}
        </div>
      </div>

      <Footer />
    </div>
  )
}
