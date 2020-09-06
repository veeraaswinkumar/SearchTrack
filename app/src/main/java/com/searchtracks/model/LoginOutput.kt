package com.searchtracks.model

import java.io.Serializable

class LoginOutput : Serializable{
    var token: String = ""
    var exp: String = ""
    var authenticated_base_url: String = ""
    var authenticated_base_url_exp: String = ""
    var email: String = ""
    var first_name: String = ""
    var middle_initial: String = ""
    var last_name: String = ""
    var user_dob: String = ""
    var user_id: String = ""
    var zip_code: String = ""
    var user_mob: String = ""
    var group_code: String = ""
    var gender: String = ""
    var mobile_terms_of_service: String = ""
    var message: String = ""
}