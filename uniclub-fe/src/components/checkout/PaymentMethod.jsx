import React from "react"
import { payment_methods } from "@/lib/mock-data"

export function PaymentMethod({ selectedId, onChange }) {
  const methods = payment_methods.filter((m) => m.status === 1)

  return (
    <div className="space-y-3">
      {methods.map((method) => (
        <label
          key={method.id}
          className={`card p-4 flex items-center gap-3 cursor-pointer transition-colors ${
            selectedId === method.id ? "ring-2 ring-ring" : ""
          }`}
        >
          <input
            type="radio"
            name="payment"
            value={method.id}
            checked={selectedId === method.id}
            onChange={() => onChange(method.id)}
            className="w-4 h-4 text-primary"
          />
          <div>
            <div className="font-medium text-foreground">{method.name}</div>
            <div className="text-sm text-muted-foreground">{method.description}</div>
          </div>
        </label>
      ))}
    </div>
  )
}
