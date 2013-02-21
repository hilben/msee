class PlatformOwner < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :encryptable, :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable ,
         :recoverable, :rememberable, :trackable, :validatable

  validates_presence_of :name
  
  validates_presence_of :username
  validates_uniqueness_of :username, :email, :case_sensitive => false

  # Setup accessible (or protected) attributes for your model
  attr_accessible :name, :username , :email, :password, :password_confirmation, :remember_me
end
