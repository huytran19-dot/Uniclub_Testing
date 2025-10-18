import React from "react"

export function Input({ className = "", ...props }) {
  const cls = `w-full h-11 px-3 py-2 rounded-md border border-border bg-white text-foreground text-sm outline-none focus:ring-2 focus:ring-ring ${className}`.trim()
  return <input className={cls} {...props} />
}

export default Input
