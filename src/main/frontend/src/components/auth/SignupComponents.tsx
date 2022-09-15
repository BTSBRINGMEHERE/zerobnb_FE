import React, {
	ChangeEvent,
	FormEvent,
	useState,
	useEffect,
	useRef,
} from "react"
import Input from "../common/Input"
import useInput from "../../hooks/useInput"
import useAuthQuery from "../../hooks/useAuthQuery"

export default function SignupComponents() {
	const { signup } = useAuthQuery()
	const isFirstRender = useRef(true)

	const inputEmail = useInput("")
	const inputPassword = useInput("")
	const inputName = useInput("")
	const inputBirth = useInput("")
	const inputPhone = useInput("")

	const [isValidEmail, setIsValidEmail] = useState(true)
	const [isValidPassword, setIsValidPassword] = useState(true)
	const [isValidName, setIsValidName] = useState(true)
	const [isValidBirth, setIsValidBirth] = useState(true)
	const [isValidPhone, setIsValidPhone] = useState(true)

	const handleSignUp = async (e: FormEvent<HTMLFormElement>) => {
		e.preventDefault()
		signup({
			email: inputEmail.value,
			password: inputPassword.value,
		})
	}

	const checkValidEmail = () => {
		const email = inputEmail.value
		return /@/.test(email) && /\./.test(email)
	}

	const checkValidPassword = () => {
		const password = inputPassword.value
		return password.length >= 8
	}

	const checkValidName = () => {
		const name = inputName.value
		return /\s/.test(name)
	}

	const checkValidBirth = () => {
		const birth = inputBirth.value
		return /^\d{2,3}-\d{3,4}-\d{4}$/.test(birth)
	}

	const checkValidPhone = () => {
		const phone = inputPhone.value
		return /^\d{3}-\d{3,4}-\d{4}$/.test(phone)
	}

	useEffect(() => {
		if (isFirstRender.current) return
		setIsValidEmail(checkValidEmail())
	}, [inputEmail.value])

	useEffect(() => {
		if (isFirstRender.current) return
		setIsValidPassword(checkValidPassword())
	}, [inputPassword.value])

	useEffect(() => {
		if (isFirstRender.current) return
		setIsValidName(checkValidName())
	}, [inputName.value])

	useEffect(() => {
		if (isFirstRender.current) return
		setIsValidBirth(checkValidBirth())
	}, [inputBirth.value])

	useEffect(() => {
		if (isFirstRender.current) return
		setIsValidPhone(checkValidPhone())
	}, [inputPhone.value])

	return (
		<>
			<div>
				<h2>회원 가입</h2>
				<form name="signup" onSubmit={handleSignUp}>
					<Input
						type="email"
						{...inputEmail}
						isValid={isValidEmail}
						invalidMessage="Email을 작성해 주세요."
					/>
					<Input
						type="password"
						placeholder="비밀번호"
						{...inputPassword}
						isValid={isValidPassword}
						invalidMessage="비밀 번호는 8자 이상"
					/>
					<Input
						type="text"
						placeholder="이름"
						{...inputName}
						isValid={isValidName}
						invalidMessage="이름"
					/>
					<Input
						type="birth"
						placeholder="1999-01-01"
						{...inputBirth}
						isValid={isValidBirth}
						invalidMessage="언제 태어 나심?"
					/>
					<Input
						type="phone"
						placeholder="010-0000-0000"
						{...inputPhone}
						isValid={isValidPhone}
						invalidMessage="오빠 오빠 폰 있어?"
					/>
					<button
						type="submit"
						disabled={
							!inputEmail.value ||
							!inputPassword.value ||
							!inputName.value ||
							!inputBirth.value ||
							!inputPhone.value
						}
					>
						회원가입
					</button>
				</form>
			</div>
		</>
	)
}
