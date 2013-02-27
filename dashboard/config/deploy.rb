set :application, "msee-website"
set :repository,  "git@sesa.sti2.at:msee-website"
set :branch, "master"
set :deploy_via, :remote_cache

set :use_sudo, false
set :user, 'deploy'
set :ssh_options, { :forward_agent => true, :compression => false}

ssh_options[:keys] = %w('~/.ssh/id_rsa.pub')

set :scm, :git
# Or: `accurev`, `bzr`, `cvs`, `darcs`, `git`, `mercurial`, `perforce`, `subversion` or `none`

set :update_vendors, true
set :deploy_to, "/var/www/rails/msee-website"

role :web, "msee.sti2.at"                          # Your HTTP server, Apache/etc
role :app, "msee.sti2.at"                          # This may be the same as your `Web` server
role :db,  "msee.sti2.at", :primary => true # This is where Rails migrations will run
#role :db,  "your slave db-server here"

set :rvm_type, :system
set :rvm_ruby_string, '1.9.3@sesa'
before 'deploy:setup', 'rvm:install_ruby'
after "deploy", "deploy:migrate"

require "rvm/capistrano"
require "bundler/capistrano"
load 'deploy/assets'

# if you're still using the script/reaper helper you will need
# these http://github.com/rails/irs_process_scripts

# If you are using Passenger mod_rails uncomment this:
namespace :deploy do
   task :start do ; end
   task :stop do ; end
   task :restart, :roles => :app, :except => { :no_release => true } do
     run "#{try_sudo} touch #{File.join(current_path,'tmp','restart.txt')}"
   end
 end
