export default function Card({ children, className = "" }) {
  return <div className={`bg-white rounded-lg border border-neutral-200 shadow-sm ${className}`}>{children}</div>
}
