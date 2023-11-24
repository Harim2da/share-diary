import { useState } from 'react';

//모달 핸들링 커스텀 훅
const useModal = (): [boolean, () => void, () => void] => {
    const [isVisible, setIsVisible] = useState(false);

    const showModal = () => {
        setIsVisible(true);
    };

    const closeModal = () => {
        setIsVisible(false);
    };

    return [isVisible, showModal, closeModal];
};

export default useModal;
