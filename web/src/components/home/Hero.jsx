import React from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Sparkles } from "lucide-react"

export default function Hero() {
  return (
    <section className="section">
      <div 
        className="relative overflow-hidden rounded-2xl card shadow-soft"
        style={{
          background: "linear-gradient(135deg, hsl(217.2 91.2% 55% / 0.08), hsl(38 92% 50% / 0.08), hsl(142 76% 36% / 0.08))"
        }}
      >
        <div className="grid md:grid-cols-2">
          <div className="p-8 md:p-12 flex flex-col justify-center gap-6">
            <div 
              className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full text-xs font-medium w-fit"
              style={{
                backgroundColor: "hsl(38 92% 50% / 0.15)",
                color: "hsl(38 92% 40%)"
              }}
            >
              <Sparkles className="w-3 h-3" />
              Bộ sưu tập mới 2025
            </div>
            <h1 className="text-4xl md:text-6xl font-bold tracking-tight leading-tight" style={{ color: "hsl(222.2 84% 8%)" }}>
              Thời trang{" "}
              <span 
                className="text-gradient"
                style={{
                  background: "linear-gradient(to right, hsl(217.2 91.2% 55%), hsl(38 92% 50%), hsl(142 76% 36%))",
                  WebkitBackgroundClip: "text",
                  backgroundClip: "text",
                  WebkitTextFillColor: "transparent",
                  display: "inline-block"
                }}
              >
                tối giản
              </span>{" "}
              cho mọi ngày
            </h1>
            <p className="text-base md:text-lg max-w-prose leading-relaxed" style={{ color: "hsl(215.4 16.3% 42%)" }}>
              Chất liệu thoải mái, thiết kế tinh gọn, phối đồ dễ dàng. Khám phá phong cách của bạn ngay hôm nay.
            </p>
            <div className="flex gap-3">
              <Button size="lg" asChild>
                <Link to="/products">Mua ngay</Link>
              </Button>
              <Button size="lg" variant="outline" asChild>
                <Link to="/products">Khám phá</Link>
              </Button>
            </div>
          </div>
          <div 
            className="relative min-h-[280px] md:min-h-[420px] rounded-b-2xl md:rounded-r-2xl md:rounded-bl-none"
            style={{
              background: "linear-gradient(135deg, hsl(217.2 91.2% 55% / 0.12), hsl(142 76% 36% / 0.12))"
            }}
          >
            <img src="/white-aline-skirt.jpg" alt="Hero" className="absolute inset-0 w-full h-full object-cover mix-blend-multiply" />
          </div>
        </div>
      </div>
    </section>
  )
}
