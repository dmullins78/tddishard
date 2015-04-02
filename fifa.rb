require 'sinatra'


configure do
    set :counter, 0
end

get '/win' do
 settings.counter += 1
 puts settings.counter

 if settings.counter % 3 == 0
    "4"
 else
    "3"
 end

end

get '/tie' do
  "1"
end
