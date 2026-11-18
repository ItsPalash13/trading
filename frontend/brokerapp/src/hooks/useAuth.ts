import { useCallback } from 'react'
import {
  api,
  useGetSessionQuery,
  useLoginMutation,
  useLogoutMutation,
  useSignupMutation,
} from '../store/api'
import { useAppDispatch } from '../store/hooks'

export function useAuth() {
  const dispatch = useAppDispatch()
  const { data: user, isLoading, refetch } = useGetSessionQuery()
  const [loginMutation] = useLoginMutation()
  const [signupMutation] = useSignupMutation()
  const [logoutMutation] = useLogoutMutation()

  const login = useCallback(
    async (email: string, password: string) => {
      await loginMutation({ email, password }).unwrap()
      await refetch()
    },
    [loginMutation, refetch],
  )

  const signup = useCallback(
    async (name: string, email: string, password: string) => {
      await signupMutation({ name, email, password }).unwrap()
      await loginMutation({ email, password }).unwrap()
      await refetch()
    },
    [loginMutation, refetch, signupMutation],
  )

  const logout = useCallback(async () => {
    try {
      await logoutMutation().unwrap()
    } finally {
      dispatch(api.util.resetApiState())
    }
  }, [dispatch, logoutMutation])

  const refresh = useCallback(async () => {
    await refetch()
  }, [refetch])

  return {
    user: user ?? null,
    loading: isLoading,
    login,
    signup,
    logout,
    refresh,
  }
}
