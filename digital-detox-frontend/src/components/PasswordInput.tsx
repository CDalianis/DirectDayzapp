import { useState, type InputHTMLAttributes } from 'react';

type PasswordInputProps = Omit<InputHTMLAttributes<HTMLInputElement>, 'type'>;

function EyeIcon() {
  return (
    <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true">
      <path
        fill="currentColor"
        d="M12 5C7 5 2.73 8.11 1 12c1.73 3.89 6 7 11 7s9.27-3.11 11-7c-1.73-3.89-6-7-11-7zm0 12a5 5 0 1 1 0-10 5 5 0 0 1 0 10zm0-2.5a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5z"
      />
    </svg>
  );
}

function EyeOffIcon() {
  return (
    <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true">
      <path
        fill="currentColor"
        d="M12 6.5c2.5 0 4.72 1.17 6.32 3H17l-1.5-1.5L12 11 8.5 7.5 7 9h1.68C10.28 7.67 12.5 6.5 15 6.5h-3zm-6.8 2.2 1.4 1.4C4.9 11.1 3.6 12.4 2.7 13.5 4.4 16.4 8 19 12 19c1.2 0 2.34-.22 3.38-.62l1.55 1.55c-1.45.62-3.05.97-4.93.97-5 0-9.27-3.11-11-7 .55-1.24 1.42-2.4 2.5-3.4l1.7 1.7zM12 17c-1.85 0-3.5-.62-4.85-1.65l1.45-1.45A4.98 4.98 0 0 0 12 15a4.98 4.98 0 0 0 3.4-1.1l1.45 1.45A6.96 6.96 0 0 1 12 17zm7.3-2.3-1.4-1.4-8.6-8.6-1.4-1.4L3.7 4.1 2.3 5.5l2.1 2.1C3.6 9.1 2.3 10.6 1.3 12c1.73 3.89 6 7 11 7 1.55 0 3.02-.32 4.36-.9l2.3 2.3 1.4-1.4-2.7-2.7z"
      />
    </svg>
  );
}

export function PasswordInput({ className, ...props }: PasswordInputProps) {
  const [visible, setVisible] = useState(false);

  return (
    <div className="password-field">
      <input
        {...props}
        type={visible ? 'text' : 'password'}
        className={className ? `password-input ${className}` : 'password-input'}
      />
      <button
        type="button"
        className="password-toggle"
        onClick={() => setVisible((current) => !current)}
        aria-label={visible ? 'Hide password' : 'Show password'}
        aria-pressed={visible}
      >
        {visible ? <EyeOffIcon /> : <EyeIcon />}
      </button>
    </div>
  );
}
