// src/hooks/useValidationRules.ts

type Rule = (v: string) => boolean | string

export function useValidationRules() {
  // 필수 입력 검사
  const requiredField = (fieldName: string): Rule[] => [
    (v: string) => !!v || `${fieldName}을 입력하세요.`
  ]

  // 이메일 패턴 검사
  const emailPatternRule: Rule[] = [
    (v: string) => /.+@.+\..+/.test(v) || '유효한 이메일 주소가 아닙니다.'
  ]

  // 최소 길이 검사
  const minLengthRule = (fieldName: string, min: number): Rule[] => [
    (v: string) => v.length >= min || `${fieldName}은(는) 최소 ${min}자 이상이어야 합니다.`
  ]

  // email용 룰
  const emailRules: Rule[] = [
    ...requiredField('이메일'),
    ...emailPatternRule,
  ]

  // password용 룰
  const passwordRules: Rule[] = [
    ...requiredField('비밀번호'),
    ...minLengthRule('비밀번호', 6),
  ]

  return { emailRules, passwordRules }
}
