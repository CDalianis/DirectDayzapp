import { zodResolver } from '@hookform/resolvers/zod';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import { apiFetch } from '../api/client';
import { PasswordInput } from '../components/PasswordInput';
import { getErrorMessage } from '../context/AuthContext';
import { coachRegisterSchema, memberRegisterSchema } from '../schemas/auth';
import { z } from 'zod';

type RegisterType = 'member' | 'coach';

export function RegisterPage() {
  const navigate = useNavigate();
  const [type, setType] = useState<RegisterType>('member');
  const [error, setError] = useState('');

  const schema = type === 'member' ? memberRegisterSchema : coachRegisterSchema;
  type FormData = z.infer<typeof schema>;

  const {
    register,
    handleSubmit,
    formState: { isSubmitting },
  } = useForm<FormData>({ resolver: zodResolver(schema) });

  const onSubmit = async (data: FormData) => {
    setError('');
    try {
      const path = type === 'member' ? '/members/register' : '/coaches/register';
      await apiFetch(path, { method: 'POST', body: JSON.stringify(data) }, false);
      navigate('/login');
    } catch (err) {
      setError(getErrorMessage(err));
    }
  };

  return (
    <section className="card narrow">
      <h1>Create account</h1>
      <div className="tabs">
        <button type="button" className={type === 'member' ? 'active' : ''} onClick={() => setType('member')}>
          Member
        </button>
        <button type="button" className={type === 'coach' ? 'active' : ''} onClick={() => setType('coach')}>
          Coach
        </button>
      </div>
      <form key={type} onSubmit={handleSubmit(onSubmit)} className="stack">
        <label>
          Username
          <input {...register('user.username')} />
        </label>
        <label>
          Email
          <input type="email" {...register('user.email')} />
        </label>
        <label>
          Password
          <PasswordInput {...register('user.password')} autoComplete="new-password" />
        </label>
        <label>
          Display name
          <input {...register('displayName')} />
        </label>
        {type === 'member' ? (
          <>
            <label>
              Timezone
              <input {...register('timezone')} placeholder="Europe/Athens" />
            </label>
            <label>
              Main goal
              <input {...register('mainGoal')} />
            </label>
          </>
        ) : (
          <>
            <label>
              Specialty
              <input {...register('specialty')} />
            </label>
            <label>
              Years of experience
              <input type="number" {...register('yearsExperience', { valueAsNumber: true })} />
            </label>
            <label>
              Bio
              <textarea {...register('bio')} rows={3} />
            </label>
          </>
        )}
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Creating...' : 'Register'}
        </button>
      </form>
      <p>
        Already registered? <Link to="/login">Sign in</Link>
      </p>
    </section>
  );
}
