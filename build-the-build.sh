#!/bin/bash

printf "#!/bin/bash\n\n" > build.sh

./mill resolve __.fullLinkJS | grep -v test | grep -v '====' | sed 's/^/.\/mill /' >> build.sh
