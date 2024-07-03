document.addEventListener('DOMContentLoaded', function() {
    const container = document.getElementById('timeline');

    fetch('/api/bookings')
        .then(response => response.json())
        .then(data => {
            const groups = new vis.DataSet();
            const items = new vis.DataSet();
            const groupVisibility = {};

            data.forEach(entry => {
                const badmintonId = 'badminton-' + entry.badmintonId;
                const courtId = 'court-' + badmintonId + entry.courtId;

                if (!groups.get(badmintonId)) {
                    groups.add({
                        id: badmintonId,
                        content: entry.badmintonName,
                        nestedGroups: [],
                        treeLevel: 1
                    });
                    groupVisibility[badmintonId] = true;  // Initially visible
                }

                groups.add({
                    id: courtId,
                    content: 'Court ' + entry.courtId + ' - ' + entry.courtDetails,
                    treeLevel: 2
                });
                groups.update({id: badmintonId, nestedGroups: groups.get(badmintonId).nestedGroups.concat([courtId])});
                groupVisibility[courtId] = true;  // Initially visible

                entry.bookings.forEach(booking => {
                    items.add({
                        id: 'booking-' + booking.id,
                        group: courtId,
                        content: booking.userName,
                        start: booking.bookingDate + 'T' + booking.startTime,
                        end: booking.bookingDate + 'T' + booking.endTime
                    });
                });
            });

            const options = {
                orientation: 'top',
                zoomMin: 1000 * 60 * 60, // 1 hour in milliseconds
                zoomMax: 1000 * 60 * 60 * 24 * 31, // 1 month in milliseconds
                start: new Date(Date.now() - 1000 * 60 * 60), // 1 hour before now
                end: new Date(Date.now() + 1000 * 60 * 60), // 1 hour after now
                editable: false,
                stack: false, // Ensure items are not stacked
                groupOrder: 'id', // Order groups by their id
            };

            const timeline = new vis.Timeline(container, items, groups, options);

            timeline.on('click', function(properties) {
                if (properties.what === 'group-label') {
                    const groupId = properties.group;
                    groupVisibility[groupId] = !groupVisibility[groupId];
                    items.get({
                        filter: function(item) {
                            return groups.get(item.group).treeLevel === 2 && item.group.startsWith(groupId);
                        }
                    }).forEach(item => {
                        items.update({id: item.id, visible: groupVisibility[groupId]});
                    });
                }
            });
        })
        .catch(error => console.error('Error fetching data:', error));
});
