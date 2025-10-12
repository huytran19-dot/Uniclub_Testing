import type React from "react"
import { cn } from "@/lib/utils"

interface Column {
  key: string
  label: string
  render?: (value: any, row: any) => React.ReactNode
}

interface DataTableProps {
  columns: Column[]
  rows: any[]
  className?: string
}

export function DataTable({ columns, rows, className }: DataTableProps) {
  return (
    <div className={cn("overflow-x-auto", className)}>
      <table className="w-full">
        <thead>
          <tr className="border-b border-border">
            {columns.map((column) => (
              <th key={column.key} className="text-left py-3 px-4 text-sm font-medium text-foreground">
                {column.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, index) => (
            <tr key={index} className="border-b border-border last:border-0 hover:bg-surface/50 transition-colors">
              {columns.map((column) => (
                <td key={column.key} className="py-3 px-4 text-sm text-muted-foreground">
                  {column.render ? column.render(row[column.key], row) : row[column.key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
