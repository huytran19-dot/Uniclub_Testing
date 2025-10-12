import type React from "react"
import { ChevronRight } from "lucide-react"
import Link from "next/link"

interface Breadcrumb {
  label: string
  href?: string
}

interface PageLayoutProps {
  title: string
  breadcrumbs?: Breadcrumb[]
  children: React.ReactNode
}

export function PageLayout({ title, breadcrumbs = [], children }: PageLayoutProps) {
  return (
    <div className="min-h-screen bg-background">
      {breadcrumbs.length > 0 && (
        <div className="bg-surface border-b border-border">
          <div className="container mx-auto px-4 py-3">
            <nav className="flex items-center gap-2 text-sm text-muted-foreground">
              <Link href="/" className="hover:text-foreground transition-colors">
                Trang chủ
              </Link>
              {breadcrumbs.map((crumb, index) => (
                <div key={index} className="flex items-center gap-2">
                  <ChevronRight className="w-4 h-4" />
                  {crumb.href ? (
                    <Link href={crumb.href} className="hover:text-foreground transition-colors">
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

      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold text-foreground mb-8">{title}</h1>
        {children}
      </div>
    </div>
  )
}
