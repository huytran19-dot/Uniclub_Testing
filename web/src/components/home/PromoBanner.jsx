import React from "react"
import { Truck, RefreshCcw, ShieldCheck } from "lucide-react"

export default function PromoBanner() {
  const items = [
    { 
      icon: Truck, 
      title: "Giao nhanh", 
      desc: "Miễn phí đơn từ 499k", 
      bgColor: "hsl(217.2 91.2% 55% / 0.1)",
      color: "hsl(217.2 91.2% 55%)"
    },
    { 
      icon: RefreshCcw, 
      title: "Đổi trả 7 ngày", 
      desc: "Miễn phí tại cửa hàng", 
      bgColor: "hsl(142 76% 36% / 0.1)",
      color: "hsl(142 76% 36%)"
    },
    { 
      icon: ShieldCheck, 
      title: "Bảo hành chất lượng", 
      desc: "Cam kết chính hãng", 
      bgColor: "hsl(38 92% 50% / 0.1)",
      color: "hsl(38 92% 50%)"
    },
  ]
  
  return (
    <section className="section">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {items.map(({ icon: Icon, title, desc, bgColor, color }) => (
          <div key={title} className="card card-hover p-6 flex items-start gap-4 group">
            <div 
              className="w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0 transition-transform group-hover:scale-110"
              style={{ backgroundColor: bgColor, color: color }}
            >
              <Icon className="w-6 h-6" />
            </div>
            <div>
              <div className="font-semibold text-foreground mb-1">{title}</div>
              <div className="text-sm text-muted-foreground leading-relaxed">{desc}</div>
            </div>
          </div>
        ))}
      </div>
    </section>
  )
}
