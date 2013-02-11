# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "activerecord-jdbcpostgresql-adapter"
  s.version = "1.2.5"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Nick Sieger, Ola Bini and JRuby contributors"]
  s.date = "2013-01-02"
  s.description = "Install this gem to use Postgres with JRuby on Rails."
  s.email = "nick@nicksieger.com, ola.bini@gmail.com"
  s.homepage = "https://github.com/jruby/activerecord-jdbc-adapter"
  s.require_paths = ["lib"]
  s.rubyforge_project = "jruby-extras"
  s.rubygems_version = "1.8.24"
  s.summary = "Postgres JDBC adapter for JRuby on Rails."

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<activerecord-jdbc-adapter>, ["~> 1.2.5"])
      s.add_runtime_dependency(%q<jdbc-postgres>, ["< 9.3", ">= 9.1"])
    else
      s.add_dependency(%q<activerecord-jdbc-adapter>, ["~> 1.2.5"])
      s.add_dependency(%q<jdbc-postgres>, ["< 9.3", ">= 9.1"])
    end
  else
    s.add_dependency(%q<activerecord-jdbc-adapter>, ["~> 1.2.5"])
    s.add_dependency(%q<jdbc-postgres>, ["< 9.3", ">= 9.1"])
  end
end
