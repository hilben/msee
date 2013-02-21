 Dir.entries("/lib/").sort.each do |entry|
   if entry =~ /.jar$/
     require entry
   end
 end

