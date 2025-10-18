import React from "react"

export function Button({ variant = "default", size = "md", className = "", style = {}, ...props }) {
  const variantStyles = {
    default: {
      backgroundColor: "hsl(var(--primary))",
      color: "hsl(var(--primary-foreground))",
    },
    secondary: {
      backgroundColor: "hsl(var(--secondary))",
      color: "hsl(var(--secondary-foreground))",
    },
    success: {
      backgroundColor: "hsl(var(--success))",
      color: "hsl(var(--success-foreground))",
    },
    warning: {
      backgroundColor: "hsl(var(--warning))",
      color: "hsl(var(--warning-foreground))",
    },
    destructive: {
      backgroundColor: "hsl(var(--destructive))",
      color: "hsl(var(--destructive-foreground))",
    },
    outline: {},
    ghost: {},
  }

  const variants = {
    default: "shadow-sm hover:opacity-90 active:opacity-80 disabled:opacity-50 disabled:pointer-events-none",
    secondary: "shadow-sm hover:opacity-90 active:opacity-80 disabled:opacity-50 disabled:pointer-events-none",
    success: "shadow-sm hover:opacity-90 active:opacity-80 disabled:opacity-50 disabled:pointer-events-none",
    warning: "shadow-sm hover:opacity-90 active:opacity-80 disabled:opacity-50 disabled:pointer-events-none",
    destructive: "shadow-sm hover:opacity-90 active:opacity-80 disabled:opacity-50 disabled:pointer-events-none",
    outline: "border-2 bg-white hover:opacity-90 disabled:opacity-50 disabled:pointer-events-none",
    ghost: "bg-transparent hover:bg-gray-100 disabled:opacity-50 disabled:pointer-events-none",
  }

  const sizes = {
    sm: "h-8 px-2.5 rounded-md text-xs font-medium",
    md: "h-9 px-4 rounded-lg text-sm font-medium",
    lg: "h-11 px-6 rounded-lg text-base font-semibold",
    icon: "h-9 w-9 inline-flex items-center justify-center rounded-lg",
  }

  const base = "inline-flex items-center justify-center gap-2 whitespace-nowrap transition-all duration-200 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2"
  const cls = `${base} ${variants[variant] || variants.default} ${sizes[size] || sizes.md} ${className}`.trim()

  const combinedStyle = { ...variantStyles[variant], ...style }

  return <button className={cls} style={combinedStyle} {...props} />
}

export default Button
