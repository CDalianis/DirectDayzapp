import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { apiFetch, type PageResponse } from '../api/client';
import type { DetoxPlan, Member } from '../api/types';
import { getErrorMessage, useAuth } from '../context/AuthContext';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { planCreateSchema } from '../schemas/auth';
import type { PlanCreateForm } from '../types/forms';

export function PlansPage() {
  const { role } = useAuth();
  const [plans, setPlans] = useState<DetoxPlan[]>([]);
  const [members, setMembers] = useState<Member[]>([]);
  const [statusFilter, setStatusFilter] = useState('');
  const [titleFilter, setTitleFilter] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState('');
  const [showCreate, setShowCreate] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<PlanCreateForm>({
    resolver: zodResolver(planCreateSchema),
    defaultValues: { status: 'ACTIVE' },
  });

  const loadPlans = async () => {
    setError('');
    try {
      const params = new URLSearchParams({ page: String(page), size: '8', sort: 'startDate,desc' });
      if (statusFilter) params.set('status', statusFilter);
      if (titleFilter) params.set('title', titleFilter);
      const data = await apiFetch<PageResponse<DetoxPlan>>(`/plans?${params}`);
      setPlans(data.content);
      setTotalPages(data.totalPages);
    } catch (err) {
      setError(getErrorMessage(err));
    }
  };

  useEffect(() => {
    void loadPlans();
  }, [page, statusFilter, titleFilter]);

  useEffect(() => {
    if (role === 'COACH' || role === 'ADMIN') {
      void apiFetch<Member[]>('/members').then(setMembers).catch(() => undefined);
    }
  }, [role]);

  const onCreatePlan = async (data: PlanCreateForm) => {
    setError('');
    try {
      await apiFetch('/plans', { method: 'POST', body: JSON.stringify(data) });
      reset();
      setShowCreate(false);
      await loadPlans();
    } catch (err) {
      setError(getErrorMessage(err));
    }
  };

  return (
    <section className="stack-lg">
      <div className="page-header">
        <h1>Detox plans</h1>
        {(role === 'COACH' || role === 'ADMIN') && (
          <button type="button" onClick={() => setShowCreate((v) => !v)}>
            {showCreate ? 'Cancel' : 'New plan'}
          </button>
        )}
      </div>

      <div className="filters row">
        <input
          placeholder="Filter by title"
          value={titleFilter}
          onChange={(e) => {
            setPage(0);
            setTitleFilter(e.target.value);
          }}
        />
        <select
          value={statusFilter}
          onChange={(e) => {
            setPage(0);
            setStatusFilter(e.target.value);
          }}
        >
          <option value="">All statuses</option>
          {['DRAFT', 'ACTIVE', 'PAUSED', 'COMPLETED', 'ARCHIVED'].map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
      </div>

      {showCreate && (
        <form className="card stack" onSubmit={handleSubmit(onCreatePlan)}>
          <h2>Create plan</h2>
          <label>
            Member
            <select {...register('memberProfileUuid')}>
              <option value="">Select member</option>
              {members.map((m) => (
                <option key={m.uuid} value={m.uuid}>
                  {m.displayName} ({m.username})
                </option>
              ))}
            </select>
            {errors.memberProfileUuid && <span className="error">{errors.memberProfileUuid.message}</span>}
          </label>
          <label>
            Title
            <input {...register('title')} />
          </label>
          <label>
            Start date
            <input type="date" {...register('startDate')} />
          </label>
          <label>
            Status
            <select {...register('status')}>
              {['DRAFT', 'ACTIVE', 'PAUSED', 'COMPLETED', 'ARCHIVED'].map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </label>
          <label>
            Target screen minutes
            <input type="number" {...register('targetScreenMinutes', { valueAsNumber: true })} />
          </label>
          <button type="submit" disabled={isSubmitting}>
            Save plan
          </button>
        </form>
      )}

      {error && <p className="error">{error}</p>}

      <div className="grid">
        {plans.map((plan) => (
          <article key={plan.uuid} className="card">
            <h3>{plan.title}</h3>
            <p className="muted">
              {plan.memberDisplayName} · Coach {plan.coachDisplayName}
            </p>
            <p>
              <span className={`badge ${plan.status.toLowerCase()}`}>{plan.status}</span>
            </p>
            <p>
              Screen target: {plan.targetScreenMinutes ?? '—'} min
            </p>
            <Link to={`/plans/${plan.uuid}`}>Open plan</Link>
          </article>
        ))}
      </div>

      <div className="pagination row">
        <button type="button" disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
          Previous
        </button>
        <span>
          Page {page + 1} of {Math.max(totalPages, 1)}
        </span>
        <button
          type="button"
          disabled={page + 1 >= totalPages}
          onClick={() => setPage((p) => p + 1)}
        >
          Next
        </button>
      </div>
    </section>
  );
}
