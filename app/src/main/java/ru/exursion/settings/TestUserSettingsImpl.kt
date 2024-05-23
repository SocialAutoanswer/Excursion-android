package ru.exursion.settings

class TestUserSettingsImpl: UserSettings {

    override val firstName: String
        get() = "Alex"
    override val lastName: String
        get() = "Fofanov"
    override val email: String
        get() = "acediatalex@gmail.com"
    override val token: String
        get() = "e6e726ef6c9de1121ea99176f3193009a3d4917ede198764bc11f9d7683eeac5"
    override val avatarImage: String
        get() = "https://wallpapers.com/images/hd/giga-chad-and-other-giga-friends-bzw34y5j87gw188g.jpg"
}