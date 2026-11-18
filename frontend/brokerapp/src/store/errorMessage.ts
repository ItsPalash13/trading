export function getErrorMessage(error: unknown, fallback: string): string {
  if (typeof error === 'object' && error !== null) {
    if ('data' in error) {
      const data = (error as { data?: unknown }).data
      if (typeof data === 'object' && data !== null && 'message' in data) {
        const message = (data as { message?: unknown }).message
        if (typeof message === 'string' && message.length > 0) {
          return message
        }
      }
      if (typeof data === 'string' && data.length > 0) {
        return data
      }
    }

    if ('error' in error) {
      const message = (error as { error?: unknown }).error
      if (typeof message === 'string' && message.length > 0) {
        return message
      }
    }
  }

  if (error instanceof Error && error.message) {
    return error.message
  }

  return fallback
}
