// File: src/main/resources/static/script.js

// Global variables
var API_BASE_URL = '/api';
var TOTAL_CAPACITY = 50;

// Run when the page loads
document.addEventListener('DOMContentLoaded', function() {
    // Open the dashboard tab by default
    openTab('dashboardTab');
    // Load initial data
    fetchVehicles();
    fetchStats();
});

// ==================== NAVIGATION FUNCTIONS ====================

// Function to switch between tabs
function openTab(tabId) {
    // Hide all tabs
    var tabButtons = document.querySelectorAll('.tab-button');
    for (var i = 0; i < tabButtons.length; i++) {
        tabButtons[i].classList.remove('active');
    }

    // Show the selected tab
    var selectedTab = document.querySelector('.tab-button[onclick="openTab(\'' + tabId + '\')"]');
    selectedTab.classList.add('active');

    // Hide all content sections
    var contentSections = document.querySelectorAll('.content');
    for (var i = 0; i < contentSections.length; i++) {
        contentSections[i].classList.remove('active');
    }

    // Show the selected content section
    document.getElementById(tabId).classList.add('active');

    // Load data based on which tab is selected
    if (tabId === 'dashboardTab') {
        fetchStats();
        fetchRecentVehicles();
    } else if (tabId === 'addVehicleTab') {
        fetchVehicles();
    } else if (tabId === 'customerDetailsTab') {
        fetchCustomers();
    } else if (tabId === 'parkingHistoryTab') {
        fetchParkingHistory();
    } else if (tabId === 'availableSlotsTab') {
        fetchSlots();
    } else if (tabId === 'parkingTicketTab') {
        // Clear previous ticket result
        document.getElementById('ticketResult').innerHTML = '';
    }
}

// ==================== DASHBOARD TAB FUNCTIONS ====================

// Fetch parking statistics
function fetchStats() {
    // Make an API call to get statistics
    fetch(API_BASE_URL + '/vehicles/stats')
        .then(function(response) {
            return response.json();
        })
        .then(function(stats) {
            // Update the dashboard with statistics
            document.getElementById('parkedCount').textContent = stats.parkedCount;
            document.getElementById('availableCount').textContent = stats.availableCount;
        })
        .catch(function(error) {
            console.error('Error fetching stats:', error);
            showError('Failed to fetch parking statistics');
        });
}

// Fetch recent vehicles
function fetchRecentVehicles() {
    // Make an API call to get recent vehicles
    fetch(API_BASE_URL + '/vehicles/recent')
        .then(function(response) {
            return response.json();
        })
        .then(function(recentVehicles) {
            displayRecentVehicles(recentVehicles);
        })
        .catch(function(error) {
            console.error('Error fetching recent vehicles:', error);
            showError('Failed to fetch recent vehicles');
        });
}

// Display recent vehicles in the dashboard
function displayRecentVehicles(recentVehicles) {
    var recentVehiclesList = document.getElementById('recentVehicles');

    if (!recentVehiclesList) return;

    // If no vehicles, show empty state
    if (recentVehicles.length === 0) {
        recentVehiclesList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-car"></i>
                <p>No recent entries</p>
            </div>`;
    } else {
        // Clear the list
        recentVehiclesList.innerHTML = "";

        // Add each vehicle to the list
        for (var i = 0; i < recentVehicles.length; i++) {
            var vehicle = recentVehicles[i];
            var entryTime = formatTime(new Date(vehicle.entryTime));

            recentVehiclesList.innerHTML += `
                <div class="vehicle-item">
                    <div class="vehicle-info">
                        <span class="vehicle-number">${vehicle.number} (${vehicle.type})</span>
                        <span class="vehicle-time">Parked at ${entryTime}</span>
                    </div>
                </div>`;
        }
    }
}

// ==================== ADD VEHICLE TAB FUNCTIONS ====================

// Fetch all currently parked vehicles
function fetchVehicles() {
    // Make an API call to get all vehicles
    fetch(API_BASE_URL + '/vehicles')
        .then(function(response) {
            return response.json();
        })
        .then(function(vehicles) {
            displayVehicles(vehicles);
        })
        .catch(function(error) {
            console.error('Error fetching vehicles:', error);
            showError('Failed to fetch vehicles');
        });
}

// Add a new vehicle
function addVehicle() {
    // Get values from form
    var vehicleNumber = document.getElementById('vehicleNumber').value.trim();
    var vehicleType = document.getElementById('vehicleType').value;
    var ownerName = document.getElementById('ownerName').value.trim();
    var ownerPhone = document.getElementById('ownerPhone').value.trim();

    // Validate input
    if (vehicleNumber === "") {
        showError("Please enter a vehicle number.");
        return;
    }

    // Create vehicle object
    var newVehicle = {
        number: vehicleNumber,
        type: vehicleType,
        owner: ownerName || "Not provided",
        phone: ownerPhone || "Not provided"
    };

    // Send API request to add vehicle
    fetch(API_BASE_URL + '/vehicles', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newVehicle)
    })
    .then(function(response) {
        if (!response.ok) {
            if (response.status === 400) {
                throw new Error("This vehicle is already parked or the parking is full!");
            } else {
                throw new Error("Failed to add vehicle. Please try again.");
            }
        }
        return response.json();
    })
    .then(function() {
        // Clear form fields
        document.getElementById('vehicleNumber').value = "";
        document.getElementById('ownerName').value = "";
        document.getElementById('ownerPhone').value = "";

        // Refresh data
        fetchVehicles();
        fetchStats();

        // Show confirmation
        showSuccess(`Vehicle ${vehicleNumber} has been parked successfully!`);
    })
    .catch(function(error) {
        console.error('Error adding vehicle:', error);
        showError(error.message);
    });
}

// Display all currently parked vehicles
function displayVehicles(vehicles) {
    var vehicleList = document.getElementById('vehicleList');

    if (!vehicleList) return;

    // If no vehicles, show empty state
    if (vehicles.length === 0) {
        vehicleList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-car"></i>
                <p>No vehicles currently parked</p>
            </div>`;
    } else {
        // Clear the list
        vehicleList.innerHTML = "";

        // Add each vehicle to the list
        for (var i = 0; i < vehicles.length; i++) {
            var vehicle = vehicles[i];
            var entryTime = formatTime(new Date(vehicle.entryTime));

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
        }
    }
}

// ==================== VEHICLE MANAGEMENT FUNCTIONS ====================

// View details of a specific vehicle
function viewDetails(number) {
    // Send API request to get vehicle details
    fetch(API_BASE_URL + '/vehicles/' + number)
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Failed to fetch vehicle details. Please try again.");
            }
            return response.json();
        })
        .then(function(vehicle) {
            var entryTime = formatTime(new Date(vehicle.entryTime));

            // Calculate parking duration in minutes
            var parkingDuration = Math.floor((new Date() - new Date(vehicle.entryTime)) / (1000 * 60));

            // Show details in an alert
            alert(`
Vehicle Details:
Number: ${vehicle.number}
Type: ${vehicle.type}
Owner: ${vehicle.owner}
Contact: ${vehicle.phone}
Entry Time: ${entryTime}
Duration: ${parkingDuration} minutes
Slot Number: ${vehicle.slotNumber}
            `);
        })
        .catch(function(error) {
            console.error('Error fetching vehicle details:', error);
            showError("Failed to fetch vehicle details. Please try again.");
        });
}

// Remove a vehicle (when it exits the parking)
function removeVehicle(number) {
    // Confirm before removing
    if (confirm(`Are you sure you want to remove vehicle ${number}?`)) {
        // Send API request to remove vehicle
        fetch(API_BASE_URL + '/vehicles/' + number, {
            method: 'DELETE'
        })
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Failed to remove vehicle. Please try again.");
            }
            return response.json();
        })
        .then(function(record) {
            showSuccess(`Vehicle ${number} has been removed. Parking fee: $${record.fee.toFixed(2)}`);

            // Refresh data
            fetchVehicles();
            fetchStats();
        })
        .catch(function(error) {
            console.error('Error removing vehicle:', error);
            showError("Failed to remove vehicle. Please try again.");
        });
    }
}

// ==================== CUSTOMER MANAGEMENT FUNCTIONS ====================

// Switch between customer tabs
function showCustomerTab(tabId) {
    // Hide all customer tabs
    var customerTabs = document.querySelectorAll('.customer-tab');
    for (var i = 0; i < customerTabs.length; i++) {
        customerTabs[i].classList.remove('active');

        // Activate the tab that matches the tabId
        if (customerTabs[i].getAttribute('data-tab') === tabId) {
            customerTabs[i].classList.add('active');
        }
    }

    // Hide all customer tab content
    var customerTabContents = document.querySelectorAll('.customer-tab-content');
    for (var i = 0; i < customerTabContents.length; i++) {
        customerTabContents[i].classList.remove('active');
    }

        // Show the selected customer tab
        var selectedTab = document.querySelector('.customer-tab[onclick="showCustomerTab(\'' + tabId + '\')"]');
        selectedTab.classList.add('active');

    // Show the selected customer tab content
    var contentEl = document.getElementById(tabId);
    if (contentEl) {
        contentEl.classList.add('active');
    }

    // Load data based on tab
    if (tabId === 'allCustomers') {
        fetchCustomers();
    } else if (tabId === 'frequentCustomers') {
        fetchFrequentCustomers();
    }
}

// Fetch all customers
function fetchCustomers() {
    // Make an API call to get all customers
    fetch(API_BASE_URL + '/customers')
        .then(function(response) {
            return response.json();
        })
        .then(function(customers) {
            displayCustomers(customers);
        })
        .catch(function(error) {
            console.error('Error fetching customers:', error);
            showError('Failed to fetch customers');
        });
}

// Fetch frequent customers
function fetchFrequentCustomers() {
    // Make an API call to get frequent customers
    fetch(API_BASE_URL + '/customers/frequent')
        .then(function(response) {
            return response.json();
        })
        .then(function(customers) {
            displayFrequentCustomers(customers);
        })
        .catch(function(error) {
            console.error('Error fetching frequent customers:', error);
            showError('Failed to fetch frequent customers');
        });
}

// Display all customers
function displayCustomers(customers) {
    var customerList = document.getElementById('customerList');

    if (!customerList) return;

    // If no customers, show empty state
    if (customers.length === 0) {
        customerList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-users"></i>
                <p>No customers found</p>
            </div>`;
    } else {
        // Clear the list
        customerList.innerHTML = "";

        // Add each customer to the list
        for (var i = 0; i < customers.length; i++) {
            var phone = customers[i];
            customerList.innerHTML += `
                <div class="vehicle-item">
                    <div class="vehicle-info">
                        <span class="vehicle-number">${phone}</span>
                    </div>
                    <div class="actions">
                        <button class="secondary" onclick="viewCustomerDetails('${phone}')">
                            <i class="fas fa-info-circle"></i> Details
                        </button>
                    </div>
                </div>`;
        }

    }
}

// Display frequent customers
function displayFrequentCustomers(customers) {
    var frequentCustomersList = document.getElementById('frequentCustomersList');

    if (!frequentCustomersList) return;

    // If no frequent customers, show empty state
    if (customers.length === 0) {
        frequentCustomersList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-users"></i>
                <p>No frequent customers found</p>
            </div>`;
    } else {
        // Clear the list
        frequentCustomersList.innerHTML = "";

        // Add each frequent customer to the list
        for (var i = 0; i < customers.length; i++) {
            var phone = customers[i];
            frequentCustomersList.innerHTML += `
                <div class="vehicle-item">
                    <div class="vehicle-info">
                        <span class="vehicle-number">${phone}</span>
                    </div>
                    <div class="actions">
                        <button class="secondary" onclick="viewCustomerDetails('${phone}')">
                            <i class="fas fa-info-circle"></i> Details
                        </button>
                    </div>
                </div>`;
        }
    }
}

// Search for a specific customer
function searchCustomer() {
    // Get phone number from input
    var phone = document.getElementById('customerPhone').value.trim();

    // Validate input
    if (phone === "") {
        showError("Please enter a phone number");
        return;
    }

    // Send API request to search for customer
    fetch(API_BASE_URL + '/customers/' + phone)
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Customer not found");
            }
            return response.json();
        })
        .then(function(customerDetails) {
            displayCustomerSearchResult(customerDetails);
        })
        .catch(function(error) {
            console.error('Error searching customer:', error);
            showError(error.message);
        });
}

// Display customer search results
function displayCustomerSearchResult(customerDetails) {
    var customerSearchResult = document.getElementById('customerSearchResult');

    if (!customerSearchResult) return;

    // If no customer details or no vehicles, show empty state
    if (!customerDetails || customerDetails.vehicles.length === 0) {
        customerSearchResult.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-user-slash"></i>
                <p>No vehicles found for this customer</p>
            </div>`;
    } else {
        // Show customer info
        customerSearchResult.innerHTML = `
            <div class="vehicle-item" style="background-color: #f0f7ff; border-left-color: #3498db;">
                <div class="vehicle-info">
                    <span class="vehicle-number">Phone: ${customerDetails.phone}</span>
                    <span class="vehicle-time">Visit Count: ${customerDetails.visitCount}</span>
                </div>
            </div>`;

        // Show customer's vehicles
        for (var i = 0; i < customerDetails.vehicles.length; i++) {
            var vehicle = customerDetails.vehicles[i];
            var entryTime = formatTime(new Date(vehicle.entryTime));

            customerSearchResult.innerHTML += `
                <div class="vehicle-item">
                    <div class="vehicle-info">
                        <span class="vehicle-number">${vehicle.number} (${vehicle.type})</span>
                        <span class="vehicle-time">Parked at ${entryTime}</span>
                    </div>
                </div>`;
        }
    }
}

// View details of a specific customer
function viewCustomerDetails(phone) {
    // Send API request to get customer details
    fetch(API_BASE_URL + '/customers/' + phone)
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Failed to fetch customer details");
            }
            return response.json();
        })
        .then(function(customerDetails) {
            // Switch to search tab and display results
            showCustomerTab('searchCustomer');
            document.getElementById('customerPhone').value = phone;
            displayCustomerSearchResult(customerDetails);
        })
        .catch(function(error) {
            console.error('Error fetching customer details:', error);
            showError(error.message);
        });
}

// ==================== PARKING SLOT MANAGEMENT FUNCTIONS ====================

// Fetch all slots
function fetchSlots() {
    showAllSlots();
}

// Show all slots
function showAllSlots() {
    // Send API request to get all slots
    fetch(API_BASE_URL + '/slots')
        .then(function(response) {
            return response.json();
        })
        .then(function(slots) {
            displaySlots(slots);

            // Update active button
            var slotFilterBtns = document.querySelectorAll('.slot-filter-btn');
            for (var i = 0; i < slotFilterBtns.length; i++) {
                slotFilterBtns[i].classList.remove('active');
            }
            document.querySelector('.slot-filter-btn:nth-child(1)').classList.add('active');
        })
        .catch(function(error) {
            console.error('Error fetching slots:', error);
            showError('Failed to fetch parking slots');
        });
}

// Show available slots
function showAvailableSlots() {
    // Send API request to get available slots
    fetch(API_BASE_URL + '/slots/available')
        .then(function(response) {
            return response.json();
        })
        .then(function(slots) {
            displaySlots(slots);

            // Update active button
            var slotFilterBtns = document.querySelectorAll('.slot-filter-btn');
            for (var i = 0; i < slotFilterBtns.length; i++) {
                slotFilterBtns[i].classList.remove('active');
            }
            document.querySelector('.slot-filter-btn:nth-child(2)').classList.add('active');
        })
        .catch(function(error) {
            console.error('Error fetching available slots:', error);
            showError('Failed to fetch available parking slots');
        });
}

// Show occupied slots
function showOccupiedSlots() {
    // Send API request to get occupied slots
    fetch(API_BASE_URL + '/slots/occupied')
        .then(function(response) {
            return response.json();
        })
        .then(function(slots) {
            displaySlots(slots);

            // Update active button
            var slotFilterBtns = document.querySelectorAll('.slot-filter-btn');
            for (var i = 0; i < slotFilterBtns.length; i++) {
                slotFilterBtns[i].classList.remove('active');
            }
            document.querySelector('.slot-filter-btn:nth-child(3)').classList.add('active');
        })
        .catch(function(error) {
            console.error('Error fetching occupied slots:', error);
            showError('Failed to fetch occupied parking slots');
        });
}

// Display slots
function displaySlots(slots) {
    var slotsList = document.getElementById('slotsList');

    if (!slotsList) return;

    // If no slots, show empty state
    if (slots.length === 0) {
        slotsList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-parking"></i>
                <p>No slots found</p>
            </div>`;
    } else {
        // Clear the list
        slotsList.innerHTML = "";

        // Add each slot to the grid
        for (var i = 0; i < slots.length; i++) {
            var slot = slots[i];
            var slotClass = slot.occupied ? 'occupied' : 'available';
            var slotIcon = slot.occupied ? 'fa-car' : 'fa-check';

            slotsList.innerHTML += `
                <div class="slot ${slotClass}">
                    <div>
                        <i class="fas ${slotIcon}"></i>
                        <div>${slot.id}</div>
                    </div>
                </div>`;
        }
    }
}

// ==================== PARKING HISTORY FUNCTIONS ====================

// Fetch all parking history
function fetchParkingHistory() {
    // Make an API call to get parking history
    fetch(API_BASE_URL + '/history')
        .then(function(response) {
            return response.json();
        })
        .then(function(history) {
            displayParkingHistory(history);
        })
        .catch(function(error) {
            console.error('Error fetching parking history:', error);
            showError('Failed to fetch parking history');
        });
}

// Search parking history by vehicle number
function searchParkingHistory() {
    var vehicleNumber = document.getElementById('historyVehicleNumber').value.trim();

    // Determine URL based on whether a vehicle number was provided
    var url = API_BASE_URL + '/history';
    if (vehicleNumber) {
        url = API_BASE_URL + '/history/vehicle/' + vehicleNumber;
    }

    // Send API request to search parking history
    fetch(url)
        .then(function(response) {
            return response.json();
        })
        .then(function(history) {
            displayParkingHistory(history);
        })
        .catch(function(error) {
            console.error('Error searching parking history:', error);
            showError('Failed to search parking history');
        });
}

// Display parking history
function displayParkingHistory(history) {
    var historyList = document.getElementById('historyList');

    if (!historyList) return;

    // If no history, show empty state
    if (history.length === 0) {
        historyList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-history"></i>
                <p>No parking history found</p>
            </div>`;
    } else {
        // Clear the list
        historyList.innerHTML = "";

        // Add each history record to the list
        for (var i = 0; i < history.length; i++) {
            var record = history[i];
            var entryTime = formatTime(new Date(record.entryTime));
            var exitTime = formatTime(new Date(record.exitTime));

            historyList.innerHTML += `
                <div class="vehicle-item">
                    <div class="vehicle-info">
                        <span class="vehicle-number">${record.vehicleNumber} (${record.vehicleType})</span>
                        <span class="vehicle-time">Entry: ${entryTime} | Exit: ${exitTime}</span>
                    </div>
                    <div class="actions">
                        <span style="font-weight: bold; color: var(--primary);">$${record.fee.toFixed(2)}</span>
                    </div>
                </div>`;
        }
    }
}

// ==================== PARKING TICKET FUNCTIONS ====================

// Generate a parking ticket
function generateTicket() {
    var vehicleNumber = document.getElementById('ticketVehicleNumber').value.trim();

    // Validate input
    if (vehicleNumber === "") {
        showError("Please enter a vehicle number");
        return;
    }

    // Send API request to generate ticket
    fetch(API_BASE_URL + '/tickets/generate/' + vehicleNumber)
        .then(function(response) {
            if (!response.ok) {
                throw new Error("Vehicle not found or ticket cannot be generated");
            }
            return response.json();
        })
        .then(function(ticket) {
            displayTicket(ticket);
        })
        .catch(function(error) {
            console.error('Error generating ticket:', error);
            showError(error.message);
        });
}

// Display a parking ticket
function displayTicket(ticket) {
    var ticketResult = document.getElementById('ticketResult');

    if (!ticketResult) return;

    // Create ticket HTML
    ticketResult.innerHTML = `
        <div class="ticket">
            <div class="ticket-header">
                <h3>Parking Ticket</h3>
                <div>${ticket.ticketNumber}</div>
            </div>
            <div class="ticket-body">
                <div class="ticket-info">
                    <div class="ticket-label">Vehicle Number</div>
                    <div class="ticket-value">${ticket.vehicleNumber}</div>
                </div>
                <div class="ticket-info">
                    <div class="ticket-label">Vehicle Type</div>
                    <div class="ticket-value">${ticket.vehicleType}</div>
                </div>
                <div class="ticket-info">
                    <div class="ticket-label">Owner Name</div>
                    <div class="ticket-value">${ticket.ownerName}</div>
                </div>
                <div class="ticket-info">
                    <div class="ticket-label">Contact</div>
                    <div class="ticket-value">${ticket.ownerPhone}</div>
                </div>
                <div class="ticket-info">
                    <div class="ticket-label">Entry Time</div>
                    <div class="ticket-value">${formatTime(new Date(ticket.entryTime))}</div>
                </div>
                <div class="ticket-info">
                    <div class="ticket-label">Slot Number</div>
                    <div class="ticket-value">${ticket.slotNumber}</div>
                </div>
                <div class="ticket-info">
                    <div class="ticket-label">Price</div>
                    <div class="ticket-value">Rs${ticket.fee}</div>
                </div>
            </div>
            <div class="ticket-footer">
                <p>Please keep this ticket safe. It will be required when exiting the parking.</p>
                <p>Issued on: ${formatTime(new Date(ticket.issueTime))}</p>
            </div>
        </div>
        <div style="text-align: center; margin-top: 20px;">
            <button onclick="printTicket()"><i class="fas fa-print"></i> Print Ticket</button>
        </div>
    `;
}

// Print a parking ticket
function printTicket() {
    var ticketContent = document.querySelector('.ticket').outerHTML;
    var printWindow = window.open('', '_blank');

    // Create a printable version of the ticket
    printWindow.document.write(`
        <html>
        <head>
            <title>Parking Ticket</title>
            <style>
                body { font-family: Arial, sans-serif; }
                .ticket {
                    width: 80mm;
                    margin: 0 auto;
                    border: 1px dashed #000;
                    padding: 10mm;
                }
                .ticket-header {
                    text-align: center;
                    margin-bottom: 5mm;
                    padding-bottom: 5mm;
                    border-bottom: 1px dashed #000;
                }
                .ticket-body {
                    margin-bottom: 5mm;
                }
                .ticket-info {
                    margin-bottom: 3mm;
                }
                .ticket-label {
                    font-size: 12px;
                    color: #666;
                }
                .ticket-value {
                    font-weight: bold;
                }
                .ticket-footer {
                    text-align: center;
                    font-size: 12px;
                    margin-top: 5mm;
                    padding-top: 5mm;
                    border-top: 1px dashed #000;
                }
            </style>
        </head>
        <body>
            ${ticketContent}
            <script>
                window.onload = function() {
                    window.print();
                    setTimeout(function() {
                        window.close();
                    }, 500);
                };
            </script>
        </body>
        </html>
    `);

    printWindow.document.close();
}

// ==================== UTILITY FUNCTIONS ====================

// Format date and time
function formatTime(date) {
    // Format time as "HH:MM AM/PM Month Day"
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var ampm = hours >= 12 ? 'PM' : 'AM';

    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0' + minutes : minutes;

    var strTime = hours + ':' + minutes + ' ' + ampm;

    // Get month name (short version)
    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    var month = months[date.getMonth()];

    return strTime + ' ' + month + ' ' + date.getDate();
}

// Show error message
function showError(message) {
    alert(message);
}

// Show success message
function showSuccess(message) {
    alert(message);
}