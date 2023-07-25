package xget.dev.jet.remote.api.creators

const val GetUserSuccessJSON = """
    {
        "id": "123e4567-e89b-12d3-a456-426655440000",
        "password": "hashed_password",
        "username": "example_username",
        "email": "example@example.com",
        "phoneNumber": "123456789",
        "profilePicture": "example_profile_picture_url"
    }
"""

const val LoginUserSuccessJSON = """
    {
        "id": "839438949839848398d3894893d489",
        "token": "ufdifdiufdifdklfdsjfuf8e78ru843fj3948fh348fiu9foc4hf38ufh43"
    }
"""

const val badRequestJSON = """
    {
        "code" : "INVALID_API_KEY",
        "message": "Bad API key. Use x-api-key in the header."
    }
"""