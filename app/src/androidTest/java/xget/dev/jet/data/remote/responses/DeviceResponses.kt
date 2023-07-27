package xget.dev.jet.data.remote.responses


const val DeviceHistorySuccessJSON = """
    {
        "history": [
          {
            "time": "2023-07-24T23:14:30.574936Z",
            "state": false,
            "user": "Juan Andrade"
          }
        ]
    }
"""

const val DeviceRecordActionSuccessJSON = """
    {
        "time": "2023-07-24T23:14:30.574936Z",
        "state": false,
        "user": "Juan Andrade"
    }
"""

const val CreateDeviceSuccessJSON = """
    {
    "id": "7834dd9322",
    "name": "Porton de Flor",
    "device_type": "GATE",
    "state": false,
    "id_user_main": "4164d97d-9aee-4d3a-8097-5cdba52e5d7d",
    "users_id": []
    }
"""

const val GetUserDevicesSuccessJSON = """
 {
    "my_devices": [
        {
            "id": "241879",
            "name": "heladera piso bajo",
            "device_type": "heladera",
            "state": false,
            "id_user_main": "123e4567-e89b-12d3-a456-426655440000",
            "users_id": [
                "123e4567-e89b-12d3-a456-426655440000",
                "cab8e67d-1f8f-43ee-b0ee-59371e26c1a0"
            ]
        }
    ],
    "other_devices": [
        {
            "id": "241879",
            "name": "heladera piso bajo",
            "device_type": "heladera",
            "state": false,
            "id_user_main": "7c4b3a1a-f081-43a9-b0e8-d3fdb92ba033",
            "users_id": [
                "19e43e68-7cf8-4bb7-b238-1092e76dda49",
                "cab8e67d-1f8f-43ee-b0ee-59371e26c1a0"
            ]
        }
    ]
}
"""

