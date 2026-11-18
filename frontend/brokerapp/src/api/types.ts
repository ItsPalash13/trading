export type User = {
  id: number
  name: string
  email: string
}

export type ApiErrorBody = {
  status?: number
  error?: string
  message?: string
  path?: string
}
