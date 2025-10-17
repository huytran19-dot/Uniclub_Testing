import React, { useState } from "react"
import { MessageCircle, Reply, Send } from "lucide-react"
import { Button } from "@/components/ui/button"
import { getCurrentUser } from "@/lib/auth"

export function CommentSection({ reviewId }) {
  const user = getCurrentUser()
  const [comments, setComments] = useState([])
  const [newComment, setNewComment] = useState("")
  const [replyingTo, setReplyingTo] = useState(null)
  const [replyContent, setReplyContent] = useState("")

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    const now = new Date()
    const diff = now - date
    const minutes = Math.floor(diff / 60000)
    const hours = Math.floor(diff / 3600000)
    const days = Math.floor(diff / 86400000)

    if (minutes < 1) return "Vừa xong"
    if (minutes < 60) return `${minutes} phút trước`
    if (hours < 24) return `${hours} giờ trước`
    if (days < 7) return `${days} ngày trước`
    return date.toLocaleDateString("vi-VN")
  }

  const handleAddComment = () => {
    if (!newComment.trim()) return

    const comment = {
      id: Date.now(),
      id_user: user.id,
      user_name: user.full_name || user.email,
      parent_id: null,
      content: newComment.trim(),
      status: 1,
      created_at: new Date().toISOString(),
      replies: [],
    }

    setComments([comment, ...comments])
    setNewComment("")
  }

  const handleAddReply = (parentId) => {
    if (!replyContent.trim()) return

    const reply = {
      id: Date.now(),
      id_user: user.id,
      user_name: user.full_name || user.email,
      parent_id: parentId,
      content: replyContent.trim(),
      status: 1,
      created_at: new Date().toISOString(),
    }

    setComments(
      comments.map((comment) =>
        comment.id === parentId
          ? { ...comment, replies: [...(comment.replies || []), reply] }
          : comment
      )
    )
    setReplyContent("")
    setReplyingTo(null)
  }

  const renderComment = (comment, isReply = false) => (
    <div key={comment.id} className={`flex gap-3 ${isReply ? "ml-12 mt-3" : ""}`}>
      <div
        className="w-8 h-8 rounded-full flex items-center justify-center text-white text-sm font-semibold flex-shrink-0"
        style={{ backgroundColor: "hsl(217.2 91.2% 55%)" }}
      >
        {comment.user_name?.charAt(0).toUpperCase() || "U"}
      </div>
      <div className="flex-1 min-w-0">
        <div className="bg-muted/30 rounded-lg p-3">
          <div className="font-semibold text-sm text-foreground mb-1">
            {comment.user_name || "Người dùng"}
          </div>
          <p className="text-sm text-foreground">{comment.content}</p>
        </div>
        <div className="flex items-center gap-3 mt-1 px-3">
          <span className="text-xs text-muted-foreground">
            {formatDate(comment.created_at)}
          </span>
          {!isReply && (
            <button
              onClick={() => setReplyingTo(comment.id)}
              className="text-xs text-primary hover:underline flex items-center gap-1"
            >
              <Reply className="w-3 h-3" />
              Trả lời
            </button>
          )}
        </div>

        {/* Reply Form */}
        {replyingTo === comment.id && (
          <div className="mt-3 flex gap-2">
            <input
              type="text"
              value={replyContent}
              onChange={(e) => setReplyContent(e.target.value)}
              placeholder="Viết trả lời..."
              className="flex-1 px-3 py-2 text-sm border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              onKeyPress={(e) => {
                if (e.key === "Enter") handleAddReply(comment.id)
              }}
            />
            <Button
              size="sm"
              onClick={() => handleAddReply(comment.id)}
              disabled={!replyContent.trim()}
            >
              <Send className="w-4 h-4" />
            </Button>
            <Button
              size="sm"
              variant="outline"
              onClick={() => {
                setReplyingTo(null)
                setReplyContent("")
              }}
            >
              Hủy
            </Button>
          </div>
        )}

        {/* Replies */}
        {comment.replies && comment.replies.length > 0 && (
          <div className="space-y-3 mt-3">
            {comment.replies.map((reply) => renderComment(reply, true))}
          </div>
        )}
      </div>
    </div>
  )

  if (!user) {
    return (
      <div className="text-center py-4 text-sm text-muted-foreground">
        Vui lòng đăng nhập để bình luận
      </div>
    )
  }

  return (
    <div className="space-y-4">
      {/* Add Comment Form */}
      <div className="flex gap-2">
        <div
          className="w-8 h-8 rounded-full flex items-center justify-center text-white text-sm font-semibold flex-shrink-0"
          style={{ backgroundColor: "hsl(217.2 91.2% 55%)" }}
        >
          {user.full_name?.charAt(0).toUpperCase() || user.email?.charAt(0).toUpperCase() || "U"}
        </div>
        <div className="flex-1 flex gap-2">
          <input
            type="text"
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            placeholder="Viết bình luận..."
            className="flex-1 px-4 py-2 text-sm border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            onKeyPress={(e) => {
              if (e.key === "Enter") handleAddComment()
            }}
          />
          <Button
            size="sm"
            onClick={handleAddComment}
            disabled={!newComment.trim()}
          >
            <Send className="w-4 h-4" />
          </Button>
        </div>
      </div>

      {/* Comments List */}
      {comments.length === 0 ? (
        <div className="text-center py-6 text-sm text-muted-foreground">
          <MessageCircle className="w-8 h-8 mx-auto mb-2 opacity-50" />
          Chưa có bình luận nào
        </div>
      ) : (
        <div className="space-y-4">
          {comments.map((comment) => renderComment(comment))}
        </div>
      )}
    </div>
  )
}
