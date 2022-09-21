import React from "react"
import { AxiosError } from "axios"
import { useMutation } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import { fetchLogin, fetchSignUp } from "../services/api/AuthAPIs"

const useAuthQuery = () => {
	const navigate = useNavigate()

	const login = useMutation(fetchLogin, {
		onSuccess: () => {
			navigate("../MainContainerPage")
		},
		onError: (error) => {
			if (error instanceof AxiosError) alert(error.response?.data.details)
		},
	}).mutate

	const signup = useMutation(fetchSignUp, {
		onSuccess: () => {
			navigate("../LoginPage")
			console.log("성공")
		},
		onError: (error) => {
			if (error instanceof AxiosError) alert(error.response?.data.details)
		},
	}).mutate

	return { login, signup }
}

export default useAuthQuery