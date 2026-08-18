# Records EMR — Frontend

A real React + Tailwind frontend for your Spring Boot `Records-EMR` backend.
Every screen calls your actual endpoints — nothing here is mock data. Login,
patient registration, visit logging, prescriptions, appointment scheduling,
and staff management all hit the real API and handle real validation errors
from `GlobalExceptionHandler`.

## Structure

```
src/
  main.jsx, App.jsx, index.css

  api/                    # one file per controller, exact endpoint matches
    client.js              # fetch wrapper: base URL, JWT header, error parsing
    auth.js                 # POST /api/auth/login
    patients.js              # /api/patients, /api/patients/register, /api/patients/{id}
    visits.js                 # /api/visits, /api/visits/patient/{id}
    appointments.js            # /api/appointments/schedule, /patient/{id}, /{id}/cancel
    prescriptions.js            # /api/prescriptions/dosage, /visit/{id}
    staff.js                     # /api/users, /api/users/create/staff, /api/users/{id}

  context/
    AuthContext.jsx          # real login/logout, JWT stored + decoded

  utils/
    jwt.js                    # decode JWT payload (reads userId/role claims)
    enums.js                   # BloodGroup/Genotype/Role — mirrors backend enums exactly
    format.js                   # age, initials, date/time formatting

  components/
    layout/                   # Sidebar, AppShell, ProtectedRoute
    common/                     # Field, Badge, Modal, ErrorBanner, EmptyState
    patients/                    # PatientRow, RegisterPatientModal, AddVisitForm,
                                   # AddPrescriptionForm, VisitCard
    appointments/                 # DoctorPicker, ScheduleAppointmentModal, AppointmentRow
    staff/                          # CreateStaffModal

  pages/
    LoginPage.jsx
    DashboardPage.jsx
    PatientsPage.jsx
    PatientDetailPage.jsx
    AppointmentsPage.jsx
    StaffPage.jsx
```

## Running it

```bash
npm install
npm run dev
```

Open the printed URL (usually `http://localhost:5173`). The Vite dev server
proxies any `/api/*` request to `http://localhost:8080`, so as long as your
Spring Boot backend is running locally on its default port, login and every
other call will work with no extra config.

Run the backend the way you normally do (`./gradlew bootRun`, Docker
Compose, etc.) with Mongo available. On first boot it seeds a default admin:

```
username: admin
password: 12345678
```

Log in with that, then use **Staff → Create account** to add doctors and
front desk users — this is a real POST to `/api/users/create/staff`.

## Two backend changes you'll want to make

I read through your controllers, services, and security config while
building this, and found two things that will block real usage. Both are
small, targeted fixes on your end — I didn't touch your backend code since
you didn't ask me to, but here's exactly what's needed.

### 1. CORS isn't configured

`SecurityConfig` has no `CorsConfigurationSource` bean, so once the frontend
is deployed anywhere other than the same origin as the backend (or even
just opened without the Vite proxy), the browser will block every request.
Add this bean and wire it into the filter chain:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5173", "https://your-deployed-frontend.com"));
    config.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

Then in `filterChain(...)`, add `.cors(cors -> {})` before `.csrf(...)`.

### 2. Front desk can't fetch the doctor list

Scheduling an appointment (`AppointmentRequest`) requires a `doctorId`, and
picking a doctor by name obviously needs a doctor list. But `GET /api/users`
in `StaffController` is `@PreAuthorize("hasRole('ADMIN')")` only — Front
Desk and Doctor accounts get a 403.

The frontend works around this today: a Doctor scheduling their own
appointment auto-fills their own ID (from the JWT), and Front Desk gets a
fallback text box for typing a doctor's staff ID if the list call is
rejected — with a note in the UI explaining why. It's usable but not great
UX. The clean fix is loosening that one endpoint:

```java
@PreAuthorize("hasAnyRole('ADMIN', 'FRONT_DESK', 'DOCTOR')")
@GetMapping
public ResponseEntity<List<StaffResponse>> getAll() {
    return ResponseEntity.ok(staffService.getAll());
}
```

(Or add a narrower `/api/users/doctors` endpoint that only returns doctors,
if you'd rather not expose full staff/username lists to non-admins.) Once
that's in place, `DoctorPicker.jsx` will automatically show the dropdown for
everyone instead of falling back — no frontend change needed.

## Notes

- Auth token is stored in `localStorage` and attached as `Authorization:
  Bearer <token>` on every request; `AuthContext` checks expiry on load and
  logs out automatically if the token has expired.
- There's no global "all appointments" or "all visits" endpoint on the
  backend (only per-patient), so the Appointments page works by searching
  for a patient first, same as the real API allows.
- Blood group values sent to the API (`A_POSITIVE`, `O_NEGATIVE`, etc.)
  match `BloodGroup.java` exactly — see `src/utils/enums.js`.
