import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import { PasswordInput } from '../components/PasswordInput';
import { getErrorMessage, useAuth } from '../context/AuthContext';
import { loginSchema } from '../schemas/auth';
import type { LoginForm } from '../types/forms';
import { useState } from 'react';

export function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginForm>({ resolver: zodResolver(loginSchema) });

  const onSubmit = async (data: LoginForm) => {
    setError('');
    try {
      await login(data);
      navigate('/plans');
    } catch (err) {
      setError(getErrorMessage(err));
    }
  };

  return (
    <section className="card narrow">
      <h1>Sign in</h1>
      <p className="muted">Use your Digital Detox account. Demo admin: <code>admin</code> / <code>Admin123!</code></p>
      <form onSubmit={handleSubmit(onSubmit)} className="stack">
        <label>
          Username
          <input {...register('username')} autoComplete="username" />
          {errors.username && <span className="error">{errors.username.message}</span>}
        </label>
        <label>
          Password
          <PasswordInput {...register('password')} autoComplete="current-password" />
          {errors.password && <span className="error">{errors.password.message}</span>}
        </label>
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Signing in...' : 'Sign in'}
        </button>
      </form>
      <p>
        No account? <Link to="/register">Register</Link>
      </p>
    </section>
  );
}
