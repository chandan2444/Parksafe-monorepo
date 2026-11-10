const API = { 
  parking: "http://32.236.119.7:8081", 
  entry: "http://32.236.119.7:8082", 
  session: "http://32.236.119.7:8080" 
};
const $ = id => document.getElementById(id);
const message = (text, error=false) => { $("message").textContent = text; $("message").style.color = error ? "#b42318" : "#245da8"; };
async function request(url, options={}) {
  const response = await fetch(url, { headers: {"Content-Type":"application/json"}, ...options });
  const raw = await response.text();
  let data = {}; try { data = raw ? JSON.parse(raw) : {}; } catch { data = raw; }
  if (!response.ok) throw new Error(data.error || data.message || `Request failed (${response.status})`);
  return data;
}
function pill(status) {
  const css = String(status || "").toLowerCase();
  return `<span class="pill ${css}">${status || "—"}</span>`;
}
async function refreshDashboard() {
  try {
    const [slots, sessions] = await Promise.all([
      request(`${API.parking}/api/parking/slots`),
      request(`${API.session}/api/sessions`)
    ]);
    const available = slots.filter(s => s.status === "AVAILABLE").length;
    const occupied = slots.filter(s => s.status === "OCCUPIED").length;
    $("totalSlots").textContent = slots.length;
    $("availableSlots").textContent = available;
    $("occupiedSlots").textContent = occupied;
    $("activeSessions").textContent = sessions.filter(s => s.status === "ACTIVE").length;
    $("slotRows").innerHTML = slots.map(s => `<tr><td>${s.slotNumber}</td><td>${s.vehicleType}</td><td>${pill(s.status)}</td></tr>`).join("") || `<tr><td colspan="3">No slots configured</td></tr>`;
    $("sessionRows").innerHTML = sessions.map(s => `<tr><td>${s.ticketNumber}</td><td>${s.vehicleNumber}</td><td>${s.slotNumber || "—"}</td><td>${pill(s.status)}</td></tr>`).join("") || `<tr><td colspan="4">No sessions yet</td></tr>`;
  } catch (e) { message(`Dashboard could not load: ${e.message}. Check that all three services and Docker containers are running.`, true); }
}
$("entryForm").addEventListener("submit", async e => {

  e.preventDefault();

  const button = e.submitter;
  button.disabled = true;

  try {

    const ticket = await request(
        `${API.entry}/api/parking/entry`,
        {
          method: "POST",
          body: JSON.stringify({
            vehicleNumber:
                $("vehicleNumber")
                    .value
                    .trim()
                    .toUpperCase(),

            vehicleType:
            $("vehicleType").value,

            ownerName:
                $("ownerName")
                    .value
                    .trim(),

            parkingLotId:
                Number(
                    $("parkingLotId").value
                )
          })
        }
    );

    $("ticketNumber").value =
        ticket.ticketNumber;

    $("ticketResult").className =
        "result";

    $("ticketResult").innerHTML =
        `
      Ticket created:
      <strong>${ticket.ticketNumber}</strong>
      <br>

      Status:
      ${pill(ticket.status)}

      <br>

      Preferred Parking Area:
      ${$("parkingLotId").options[$("parkingLotId").selectedIndex].text}

      <br>

      Wait a moment, then search this ticket again for allocation status.
      `;

    message(
        "Vehicle entry submitted. Waiting for RabbitMQ slot allocation."
    );

    $("entryForm").reset();

    setTimeout(
        refreshDashboard,
        1000
    );

  } catch (err) {

    message(
        err.message,
        true
    );

  } finally {

    button.disabled = false;

  }
});
$("ticketForm").addEventListener("submit", async e => {
  e.preventDefault();
  try {
    const t = await request(`${API.entry}/api/parking/tickets/${encodeURIComponent($("ticketNumber").value.trim())}`);
    $("ticketResult").className = "result";
    $("ticketResult").innerHTML = `<strong>${t.ticketNumber}</strong><br>Vehicle: ${t.vehicleNumber}<br>Type: ${t.vehicleType}<br>Status: ${pill(t.status)}<br>Slot: ${t.slotNumber || "Not allocated yet"}<br>Entry: ${t.entryTime || "—"}<br>Exit: ${t.exitTime || "—"}`;
    message("Ticket details loaded.");
  } catch (err) { message(err.message, true); }
});
$("exitForm").addEventListener("submit", async e => {
  e.preventDefault();
  try {
    const t = await request(`${API.entry}/api/parking/exit/${encodeURIComponent($("exitTicket").value.trim())}`, {method:"POST"});
    message(`Exit request accepted for ${t.ticketNumber}. Wait for session completion and slot release.`);
    setTimeout(refreshDashboard, 1200);
  } catch (err) { message(err.message, true); }
});
$("refreshAll").addEventListener("click", refreshDashboard);
refreshDashboard();
setInterval(refreshDashboard, 10000);
