let vehicles = [];
const TOTAL_CAPACITY = 50;
const API_URL = 'http://localhost:8080/api/vehicles';

// Initialization
document.addEventListener('DOMContentLoaded', function() {
    fetchVehicles();
    fetchStats();
});

function openTab(tabId) {
    // Remove active class from all tabs
    document.querySelectorAll('.tab-button').forEach(tab => {
        tab.classList.remove('active');
    });

    // Add active class to clicked tab
    document.querySelector(`.tab-button[onclick="openTab('${tabId}')"]`).classList.add('active');

    // Hide all content sections
    document.querySelectorAll('.content').forEach(content => {
        content.classList.remove('active');
    });

    // Show selected content section
    document.getElementById(tabId).classList.add('active');

    // Refresh data when switching to dashboard
    if (tabId === 'dashboardTab') {
        fetchStats();
        fetchRecentVehicles();
    } else if (tabId === 'addVehicleTab') {
        fetchVehicles();
    }
}

async function fetchVehicles() {
    try {
        const response = await fetch(API_URL);
        vehicles = await response.json();
        displayVehicles();
    } catch (error) {
        console.error('Error fetching vehicles:', error);
    }
}

async function fetchStats() {
    try {
        const response = await fetch(`${API_URL}/stats`);
        const stats = await response.json();

        document.getElementById('parkedCount').textContent = stats.parkedCount;
        document.getElementById('availableCount').textContent = stats.availableCount;
    } catch (error) {
        console.error('Error fetching stats:', error);
    }
}

async function fetchRecentVehicles() {
    try {
        const response = await fetch(`${API_URL}/recent`);
        const recentVehicles = await response.json();

        displayRecentVehicles(recentVehicles);
    } catch (error) {
        console.error('Error fetching recent vehicles:', error);
    }
}

async function addVehicle() {
    const vehicleNumber = document.getElementById('vehicleNumber').value.trim();
    const vehicleType = document.getElementById('vehicleType').value;
    const ownerName = document.getElementById('ownerName').value.trim();
    const ownerPhone = document.getElementById('ownerPhone').value.trim();

    if (vehicleNumber === "") {
        alert("Please enter a vehicle number.");
        return;
    }

    const newVehicle = {
        number: vehicleNumber,
        type: vehicleType,
        owner: ownerName || "Not provided",
        phone: ownerPhone || "Not provided"
    };

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newVehicle)
        });

        if (!response.ok) {
            if (response.status === 400) {
                alert("This vehicle is already parked or the parking is full!");
            } else {
                alert("Failed to add vehicle. Please try again.");
            }
            return;
        }

        // Clear form fields
        document.getElementById('vehicleNumber').value = "";
        document.getElementById('ownerName').value = "";
        document.getElementById('ownerPhone').value = "";

        // Refresh data
        fetchVehicles();
        fetchStats();

        // Show confirmation
        alert(`Vehicle ${vehicleNumber} has been parked successfully!`);
    } catch (error) {
        console.error('Error adding vehicle:', error);
        alert("Failed to add vehicle. Please try again.");
    }
}

async function removeVehicle(number) {
    if (confirm(`Are you sure you want to remove vehicle ${number}?`)) {
        try {
            const response = await fetch(`${API_URL}/${number}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                alert("Failed to remove vehicle. Please try again.");
                return;
            }

            // Refresh data
            fetchVehicles();
            fetchStats();
        } catch (error) {
            console.error('Error removing vehicle:', error);
            alert("Failed to remove vehicle. Please try again.");
        }
    }
}

function displayVehicles() {
    const vehicleList = document.getElementById('vehicleList');

    if (!vehicleList) return;

    if (vehicles.length === 0) {
        vehicleList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-car"></i>
                <p>No vehicles currently parked</p>
            </div>`;
    } else {
        vehicleList.innerHTML = "";
        vehicles.forEach((vehicle) => {
            const entryTime = formatTime(new Date(vehicle.entryTime));

            vehicleList.innerHTML += `
                <div class="vehicle-item">
                    <div class="vehicle-info">
                        <span class="vehicle-number">${vehicle.number} (${vehicle.type})</span>
                        <span class="vehicle-time">Parked at ${entryTime}</span>
                    </div>
                    <div class="actions">
                        <button class="secondary" onclick="viewDetails('${vehicle.number}')">
                            <i class="fas fa-info-circle"></i> Details
                        </button>
                        <button class="danger" onclick="removeVehicle('${vehicle.number}')">
                            <i class="fas fa-sign-out-alt"></i> Exit
                        </button>
                    </div>
                </div>`;
        });
    }
}

function displayRecentVehicles(recentVehicles) {
    const recentVehiclesList = document.getElementById('recentVehicles');

    if (!recentVehiclesList) return;

    if (recentVehicles.length === 0) {
        recentVehiclesList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-car"></i>
                <p>No recent entries</p>
            </div>`;
    } else {
        recentVehiclesList.innerHTML = "";
        recentVehicles.forEach((vehicle) => {
            const entryTime = formatTime(new Date(vehicle.entryTime));

            recentVehiclesList.innerHTML += `
                <div class="vehicle-item">
                    <div class="vehicle-info">
                        <span class="vehicle-number">${vehicle.number} (${vehicle.type})</span>
                        <span class="vehicle-time">Parked at ${entryTime}</span>
                    </div>
                </div>`;
        });
    }
}

async function viewDetails(number) {
    try {
        const response = await fetch(`${API_URL}/${number}`);

        if (!response.ok) {
            alert("Failed to fetch vehicle details. Please try again.");
            return;
        }

        const vehicle = await response.json();
        const entryTime = formatTime(new Date(vehicle.entryTime));

        const parkingDuration = Math.floor((new Date() - new Date(vehicle.entryTime)) / (1000 * 60));

        alert(`
Vehicle Details:
Number: ${vehicle.number}
Type: ${vehicle.type}
Owner: ${vehicle.owner}
Contact: ${vehicle.phone}
Entry Time: ${entryTime}
Duration: ${parkingDuration} minutes
        `);
    } catch (error) {
        console.error('Error fetching vehicle details:', error);
        alert("Failed to fetch vehicle details. Please try again.");
    }
}

function formatTime(date) {
    return date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
}